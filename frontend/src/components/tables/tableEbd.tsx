/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useState } from "react";
import Table  from "react-bootstrap/Table";
import { fetchEbds } from "../../utils/api";
import Pagination from "react-bootstrap/Pagination";
import { Ebd } from "../../types/ebd";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import { Link } from "react-router-dom";


function DataTableEbd() {

  const [ebd, setEbd] =  useState<Ebd[]>([])
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);
  const [anoSearch, setAnoSearch] = useState('');

  useEffect(() => {
      fetchEbds().then(response => setEbd(response.data))
      .catch(error => console.log(error))
  }, []);

  const handlePageChange = (page: number) => {
      setCurrentPage(page);
  };

  
  const lowerSearch = search.toLowerCase();
    const filteredEbds = ebd.filter(ebd => (
      ebd.nome.toLowerCase().includes(lowerSearch)
  ));

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentEbds = filteredEbds.slice(
      indexOfFirstItem,
      indexOfLastItem
  );    

  return (
    <>
        {/* Busca + botão */}
        <div className="flex justify-between items-center w-full mb-4 gap-4">

            <input
                className="text-center bg-slate-500 text-white h-10 w-64 rounded-lg px-3"
                type="text"
                value={search}
                onChange={event => setSearch(event.target.value)}
                placeholder="Digite nome"
            />

            <a
                href="/escolabiblica/new/0"
                className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md shrink-0 mt-12"
                title="NOVO"
            >
                <BsFillPlusCircleFill className="w-8 h-8 text-white" />
            </a>

        </div>

        {/* Tabela */}
        <div className="overflow-x-auto rounded-xl shadow-md">

            <Table
                striped
                bordered
                hover
                variant="dark"
                className="min-w-[900px] mb-0"
            >

                <thead>
                    <tr>
                        <th></th>
                        <th>Nome</th>
                        <th>Coordenador</th>
                        <th>ViceCoordenador</th>
                        <th>Presbítero</th>
                        <th>Ação</th>
                    </tr>
                </thead>

                <tbody>
                    {currentEbds.map((ebd, index) => (
                        <tr key={ebd.id}>

                            <td>{index + 1}</td>

                            <td>{ebd.nome}</td>

                            <td>{ebd.coordenador}</td>

                            <td>{ebd.viceCoordenador}</td>

                            <td>{ebd.presbitero}</td>

                            <td>
                                <div className="flex justify-center">

                                    <Link to={`/escolabiblica/${ebd.id}`}>

                                        <button
                                            className="w-10 h-10 flex items-center justify-center"
                                            title="EDITAR"
                                        >
                                            <BsPencil
                                                className="w-5 h-5"
                                                color="yellow"
                                            />
                                        </button>

                                    </Link>

                                </div>
                            </td>

                        </tr>
                    ))}
                </tbody>

            </Table>

        </div>

        {/* Paginação */}
        <div className="flex justify-center mt-4 overflow-x-auto">

            <Pagination>

                <Pagination.Prev
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 1}
                />

                {[...Array(Math.ceil(filteredEbds.length / itemsPerPage))].map(
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
                        Math.ceil(filteredEbds.length / itemsPerPage)
                    }
                />

            </Pagination>

        </div>
    </>
);

}

export default DataTableEbd;