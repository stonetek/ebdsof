/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useState } from "react";
import Table  from "react-bootstrap/Table";
import { Igreja } from "../../types/igreja";
import { fetchIgrejaById, fetchIgrejas } from "../../utils/api";
import Pagination from "react-bootstrap/Pagination";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import { Link } from "react-router-dom";
import { MdOutbond } from "react-icons/md";



function DataTableIgreja() {

  const [igreja, setIgreja] =  useState<Igreja[]>([])
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);
  const igrejaId = sessionStorage.getItem('igrejaId');
  



  useEffect(() => {
    const igrejaId = sessionStorage.getItem('igrejaId');
    if (igrejaId && !isNaN(Number(igrejaId))) {
      fetchIgrejaById(Number(igrejaId))
        .then(response => setIgreja([response.data]))
        .catch(error => console.log(error));
    } else {
      fetchIgrejas()
        .then(response => setIgreja(response.data))
        .catch(error => console.log(error));
    }
  }, []);
  
  

  const handlePageChange = (page: number) => {
        setCurrentPage(page);
  };

    
    const lowerSearch = search.toLowerCase();
    const filteredIgrejas = igreja.filter((igreja) => igreja.
        nome.toLowerCase().includes(lowerSearch));

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentIgrejas = filteredIgrejas.slice(
        indexOfFirstItem,
        indexOfLastItem
    );    



    return (
    <>
        {/* Barra superior */}
        <div className="flex flex-row md:flex-row justify-between items-center gap-2 mb-4 w-full">

            {/* Pesquisa */}
            <div className="w-full md:w-1/4">
                <input
                    className="w-full h-10 rounded-lg px-3 text-center bg-slate-500 text-white mb-5"
                    type="text"
                    value={search}
                    onChange={event => setSearch(event.target.value)}
                    placeholder="Digite nome"
                />
            </div>

            {/* Botão novo */}
            {(!igrejaId || igrejaId === 'null') && (
                <a
                    href="/igrejas/new/0"
                    className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md mt-12"
                    title="NOVO"
                >
                    <BsFillPlusCircleFill className="w-8 h-8 text-white" />
                </a>
            )}
        </div>

        {/* Tabela responsiva */}
        <div className="overflow-x-auto">
            <Table striped bordered hover variant="dark">
                <thead>
                    <tr>
                        <th></th>
                        <th>Nome</th>
                        <th>Endereço</th>
                        <th>Bairro</th>
                        <th>Cidade</th>
                        <th>CEP</th>
                        <th>Área</th>
                        <th>Ação</th>
                    </tr>
                </thead>

                <tbody>
                    {currentIgrejas.map((igreja, index) => (
                        <tr key={igreja.id}>
                            <td>{index + 1}</td>
                            <td>{igreja.nome}</td>
                            <td>{igreja.endereco}</td>
                            <td>{igreja.bairro}</td>
                            <td>{igreja.cidade}</td>
                            <td>{igreja.cep}</td>
                            <td>{igreja.area}</td>

                            <td>
                                <div className="flex gap-2 justify-center">

                                    <Link to={`/igrejas/${igreja.id}`}>
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

                                    <Link to={`/igrejaEebdEturma/${igreja.id}`}>
                                        <button
                                            className="w-10 h-10 flex items-center justify-center"
                                            title="Listar ebd e turmas"
                                        >
                                            <MdOutbond
                                                className="w-5 h-5"
                                                color="blue"
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
        <div className="flex justify-center mt-4">
            <Pagination>
                <Pagination.Prev
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 1}
                />

                {[...Array(Math.ceil(filteredIgrejas.length / itemsPerPage))].map(
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
                        Math.ceil(filteredIgrejas.length / itemsPerPage)
                    }
                />
            </Pagination>
        </div>
    </>
);

}

export default DataTableIgreja;