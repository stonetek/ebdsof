/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useState } from "react";
import Header from "../../../components/header/header";
import { fetchEbdTurmas } from "../../../utils/api";
import { Link } from "react-router-dom";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import { EbdTurmas } from "../../../types/ebdTurmas";
import { Button, Pagination } from "react-bootstrap";
import Footer from "../../../components/footer/footer";

function EbdAndTurmas() {

  const [ebdTurmas, setEbdTurmas] = useState<EbdTurmas[]>([]);
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);


    useEffect(() => {
        fetchEbdTurmas().then(response => setEbdTurmas(response.data))
        .catch(error => console.log(error))
    }, []);

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
  };

  const lowerSearch = search.toLowerCase();
  const filteredEbdTurmas = ebdTurmas.filter((turma) => turma.
    nomeTurma.toLowerCase().includes(lowerSearch));

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentEbdTurmas = filteredEbdTurmas.slice(
    indexOfFirstItem,
    indexOfLastItem
  );    


    return (
        <>
            <Header/>
            <h2 className="text-2xl font-bold text-center mb-4">Ebd e Turmas Associadas</h2>
            <div className="container mx-auto px-4 w-screen h-screen">

                <div className="flex justify-between items-center w-full mb-4 gap-4">   
                  <input
                    className="text-center border border-gray-300 bg-white h-10 w-64 rounded-lg px-3 shadow-sm"
                    type="text"
                    value={search}
                    onChange={event => setSearch(event.target.value)}
                    placeholder="Nome Turma"
                  />
                    <a href="/escolabiblicaEClasses/new/0" className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md shrink-0 mt-12" title="NOVO">
                        <BsFillPlusCircleFill className="w-8 h-8 text-white"/>
                    </a>
                </div>

                {/* Tabela */}
                <div className="overflow-x-auto bg-white rounded-lg shadow overflow-y-auto relative">
                    <table className="border-collapse table-auto w-full whitespace-no-wrap bg-white table-striped relative">
                        <thead>
                            <tr className="text-left">
                                <th className="py-2 px-3 sticky top-0 border-b border-gray-200 bg-gray-100"></th>
                                <th className="py-2 px-3 sticky top-0 border-b border-gray-200 bg-gray-100">Nome da Turma</th>
                                <th className="py-2 px-3 sticky top-0 border-b border-gray-200 bg-gray-100">Nome da Ebd</th>
                                <th className="py-2 px-3 sticky top-0 border-b border-gray-200 bg-gray-100">Ação</th>
                            </tr>
                        </thead>
                        <tbody>
                            {currentEbdTurmas.map((item, index) => (
                                <tr key={item.id} className="border-b border-gray-200">
                                    <td className="flex justify-center p-2">{index + 1}</td>
                                    <td className="py-2 px-3">{item.nomeTurma}</td>
                                    <td className="py-2 px-3">{item.nomeEbd}</td>
                                    <td>
                                        <div>
                                            <Link to={`/escolabiblicaEClasses/${item.id}`}>
                                                <button className="w-14 h-10 flex-col" title="EDITAR">
                                                    <BsPencil className="w-20 h-6 mt-2" color="green"/>
                                                </button>
                                            </Link>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <Pagination className="justify-content-center mt-4">
                    <Pagination.Prev
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 1}
                    />
                    {[...Array(Math.ceil(filteredEbdTurmas.length / itemsPerPage))].map(
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
                    disabled={currentPage === Math.ceil(filteredEbdTurmas.length / itemsPerPage)}
                    />
              </Pagination>
                </div>
                <Button as="a" href="/escolabiblica" className="mt-4">VOLTAR</Button>
            </div>
            <footer>
                <Footer />
            </footer>
        </>
    )
}

export default EbdAndTurmas;


