import { useEffect, useState } from "react";
import Table  from "react-bootstrap/Table";
import { fetchProfessores, fetchProfessorPorIgreja } from "../../utils/api";
import Pagination from "react-bootstrap/Pagination";
import { Professor } from "../../types/professor";
import { formatLocalDate } from "../../utils/format";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import { Link } from "react-router-dom";



function DataTableProfessor() {

    const [professor, setProfessor] =  useState<Professor[]>([])
    const [search, setSearch] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage] = useState(5);

    useEffect(() => {
      const igrejaId = sessionStorage.getItem('igrejaId');
      if (igrejaId && !isNaN(Number(igrejaId))){
      fetchProfessorPorIgreja(Number(igrejaId))
        .then(response => setProfessor(response.data))
        .catch(error => console.log(error));
      } else {
        fetchProfessores().then(response => setProfessor(response.data))
        .catch(error => console.log(error))
      }
      }, []);

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
      };

    
    const lowerSearch = search.toLowerCase();
    const filteredProfessores = professor.filter((professor) => professor.
        nome.toLowerCase().includes(lowerSearch));

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentProfessores = filteredProfessores.slice(
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
                href="/professores/new/0"
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
                className="min-w-[600px] mb-0"
            >

                <thead>
                    <tr>
                        <th></th>
                        <th>Nome</th>
                        <th>Aniversário</th>
                        <th>Ação</th>
                    </tr>
                </thead>

                <tbody>
                    {currentProfessores.map((professor, index) => (
                        <tr key={professor.id}>

                            <td>{index + 1}</td>

                            <td>{professor.nome}</td>

                            <td>
                                {formatLocalDate(
                                    professor.aniversario,
                                    'dd/MM/yyyy'
                                )}
                            </td>

                            <td>
                                <div className="flex justify-center">

                                    <Link to={`/professores/${professor.id}`}>

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

                {[...Array(Math.ceil(filteredProfessores.length / itemsPerPage))].map(
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
                        Math.ceil(filteredProfessores.length / itemsPerPage)
                    }
                />

            </Pagination>

        </div>
    </>
);

}

export default DataTableProfessor;