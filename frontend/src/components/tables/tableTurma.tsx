import { useEffect, useState } from "react";
import Table  from "react-bootstrap/Table";
import { fetchProfessorTurma, fetchTurmas, fetchTurmasPorIgreja } from "../../utils/api";
import Pagination from "react-bootstrap/Pagination";
import { Turma } from "../../types/turma";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import { Link } from "react-router-dom";



function DataTableTurma() {

  const [turma, setTurma] =  useState<Turma[]>([])
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);
    const nomePerfil = sessionStorage.getItem('nomePerfil');
    const showNovo = nomePerfil !== 'PROFESSOR';

  useEffect(() => {
    const igrejaId = sessionStorage.getItem('igrejaId');
    const classeId = sessionStorage.getItem("classeId");

    const carregarTurmas = async () => {
      try {
            if (classeId && !isNaN(Number(classeId))) {
                const response = await fetchProfessorTurma(Number(classeId));
                setTurma(response.data);
            } else if (igrejaId && !isNaN(Number(igrejaId))) {
                const response = await fetchTurmasPorIgreja(Number(igrejaId));
                setTurma(response.data);
            } else {
                const response = await fetchTurmas();
                setTurma(response.data);
            }
      } catch (error) {
            console.error("Erro ao buscar turmas:", error);
        }
    };

    carregarTurmas();
}, []);


  const handlePageChange = (page: number) => {
        setCurrentPage(page);
  };

    
  const lowerSearch = search.toLowerCase();
  const filteredTurmas = turma.filter((turma) => turma.
    nome.toLowerCase().includes(lowerSearch));

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentTurmas = filteredTurmas.slice(
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

            {showNovo && (
                <a
                    href="/classes/new/0"
                    className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md shrink-0 mt-12"
                    title="NOVO"
                >
                    <BsFillPlusCircleFill className="w-8 h-8 text-white" />
                </a>
            )}

        </div>

        {/* Tabela */}
        <div className="overflow-x-auto rounded-xl shadow-md">

            <Table
                striped
                bordered
                hover
                variant="dark"
                className="min-w-[700px] mb-0"
            >

                <thead>
                    <tr>
                        <th></th>
                        <th>Classes</th>
                        <th>Idade Mínima</th>
                        <th>Idade Máxima</th>
                        <th>Ação</th>
                    </tr>
                </thead>

                <tbody>
                    {currentTurmas.map((turma, index) => (
                        <tr key={turma.id}>

                            <td>{index + 1}</td>

                            <td>{turma.nome}</td>

                            <td>{turma.idadeMinima}</td>

                            <td>{turma.idadeMaxima}</td>

                            <td>
                                <div className="flex justify-center">

                                    <Link to={`/classes/${turma.id}`}>

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

                {[...Array(Math.ceil(filteredTurmas.length / itemsPerPage))].map(
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
                        Math.ceil(filteredTurmas.length / itemsPerPage)
                    }
                />

            </Pagination>

        </div>
    </>
);

}

export default DataTableTurma;