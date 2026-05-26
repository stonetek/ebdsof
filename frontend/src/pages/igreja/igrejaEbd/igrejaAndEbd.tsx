/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useState } from "react";
import { IgrejaEbd } from "../../../types/igrejaEbd";
import { fetchIgrejaEbd, fetchIgrejaEbdVinculo } from "../../../utils/api";
import { Link } from "react-router-dom";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import Header from "../../../components/header/header";
import { Button, Pagination } from "react-bootstrap";

function IgrejaAndEbd() {
  const [igrejaEbd, setIgrejaEbd] = useState<IgrejaEbd[]>([]);
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);
  const [idIgreja, setIdIgreja] = useState<number | null>(null);
  
  
  useEffect(() => { 
    const storedIgrejaId = sessionStorage.getItem('igrejaId');
    //console.log('Stored Igreja ID:', storedIgrejaId);
  
    if (storedIgrejaId && storedIgrejaId !== 'null') {
      const idIgrejaNumber = Number(storedIgrejaId);
      setIdIgreja(idIgrejaNumber);
      
      fetchIgrejaEbdVinculo(idIgrejaNumber)
        .then(response => {
          //console.log('Response from fetchIgrejaEbdVinculo:', response.data);
          setIgrejaEbd([response.data]); // Atualizando como array com os dados
        })
        .catch(error => console.log(error));
    } else {
      fetchIgrejaEbd()
        .then(response => {
          console.log('Response from fetchIgrejaEbd:', response.data);
          setIgrejaEbd(response.data);
        })
        .catch(error => console.log(error));
    }
  }, []);
  
  
  
  
  const handlePageChange = (page: number) => {
          setCurrentPage(page);
  };

  
  
  const lowerSearch = search.toLowerCase();
  const filteredIgrejaEbds = Array.isArray(igrejaEbd) ? igrejaEbd.filter((igreja) => 
    igreja.nomeIgreja.toLowerCase().includes(lowerSearch)
  ) : [];
  
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentIgrejaEbds = filteredIgrejaEbds.length > 0 ? filteredIgrejaEbds.slice(
    indexOfFirstItem,
    indexOfLastItem
  ) : filteredIgrejaEbds;
  
  
  return (
  <>
    <Header />

    <div className="container mx-auto px-4 mt-4 min-h-screen">

      {/* Título */}
      <div className="flex justify-center mb-4">
        <h2 className="text-2xl font-bold text-center">
          Vínculo Igreja Ebd
        </h2>
      </div>

      {/* Pesquisa + botão */}
      <div className="flex justify-between items-center mb-3 gap-4">

        <input
          className="text-center border border-gray-300 rounded-lg bg-gray-100 h-10 px-3 mb-10"
          type="text"
          value={search}
          onChange={event => setSearch(event.target.value)}
          placeholder="Nome Igreja"
        />

        <a
          href="/igrejaEebd/new/0"
          className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md"
          title="NOVO"
        >
          <BsFillPlusCircleFill className="w-8 h-8 text-white" />
        </a>

      </div>

      {/* Tabela */}
      <div className="overflow-x-auto bg-white rounded-lg shadow relative">

        <table className="border-collapse table-auto w-full bg-white">
          <thead>
            <tr className="text-left">
              <th className="py-2 px-3 border-b bg-gray-100"></th>
              <th className="py-2 px-3 border-b bg-gray-100">
                Nome da Igreja
              </th>
              <th className="py-2 px-3 border-b bg-gray-100">
                Nome EBD
              </th>
              <th className="py-2 px-3 border-b bg-gray-100">
                Ação
              </th>
            </tr>
          </thead>

          <tbody>
            {currentIgrejaEbds.map((item, index) => (
              <tr key={item.id} className="border-b border-gray-200">

                <td className="text-center p-2">
                  {index + 1}
                </td>

                <td className="py-2 px-3">
                  {item.nomeIgreja}
                </td>

                <td className="py-2 px-3">
                  {item.nomeEbd}
                </td>

                <td>
                  <div className="flex justify-center">
                    <Link to={`/igrejaEebd/${item.id}`}>
                      <button
                        className="w-10 h-10 flex items-center justify-center"
                        title="EDITAR"
                      >
                        <BsPencil
                          className="w-5 h-5"
                          color="green"
                        />
                      </button>
                    </Link>
                  </div>
                </td>

              </tr>
            ))}
          </tbody>
        </table>

        {/* Paginação */}
        <Pagination className="justify-content-center mt-4 pb-3">

          <Pagination.Prev
            onClick={() => handlePageChange(currentPage - 1)}
            disabled={currentPage === 1}
          />

          {[...Array(Math.ceil(filteredIgrejaEbds.length / itemsPerPage))].map(
            (_, index) => (
              <Pagination.Item
                key={index}
                active={index + 1 === currentPage}
                onClick={() => handlePageChange(index + 1)}
              >
                {index + 1}
              </Pagination.Item>
            )
          )}

          <Pagination.Next
            onClick={() => handlePageChange(currentPage + 1)}
            disabled={
              currentPage ===
              Math.ceil(filteredIgrejaEbds.length / itemsPerPage)
            }
          />

        </Pagination>

      </div>

      <Button as="a" href="/igrejas" className="mt-3">
        VOLTAR
      </Button>

    </div>
  </>
)
}

export default IgrejaAndEbd;