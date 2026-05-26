/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useState } from "react";
import Table  from "react-bootstrap/Table";
import { fetchAulas, fetchAulasPorIgreja, fetchProfessorAula } from "../../utils/api";
import Pagination from "react-bootstrap/Pagination";
import { Aula } from "../../types/aula";
import { formatLocalDate } from "../../utils/format";
import { Link } from "react-router-dom";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import { Aluno } from "../../types/aluno";
import AlunosModal from "../../pages/aluno/modal/alunoModal";


interface alunoAulas {
  id: number;
  idAluno: number;
  nomeAluno: string;
  idAula: number;
  licao: string;
  presente: boolean;
  nome: string
}


function DataTableAula() {

  const [aula, setAula] =  useState<Aula[]>([])
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);
  const [showModal, setShowModal] = useState(false);
  const [selectedAlunosPorTurma, setSelectedAlunosPorTurma] = useState<Aluno[]>([]);
  const [selectedAlunoAulas, setSelectedAlunoAulas] = useState<alunoAulas[]>([]);
  const [selectedTurma, setSelectedTurma] = useState<string>('');
  const nomePerfil = sessionStorage.getItem('nomePerfil');

  useEffect(() => {
    const igrejaId = sessionStorage.getItem('igrejaId');
    const classeId = sessionStorage.getItem("classeId");

    const carregarAulas = async () => {
      try {
        if (classeId && !isNaN(Number(classeId))) {
          const response = await fetchProfessorAula(Number(classeId));
            setAula(response.data);
            } else if (igrejaId && !isNaN(Number(igrejaId))) {
                const response = await fetchAulasPorIgreja(Number(igrejaId));
                setAula(response.data);
            } else {
                const response = await fetchAulas();
                setAula(response.data);
            }
      } catch (error) {
        console.error("Erro ao buscar aulas:", error);
      }
    };

    carregarAulas();
  }, []);


  const handlePageChange = (page: number) => {
        setCurrentPage(page);
  };

  const handleShowModal = (aulasTurmas:any, alunoAulas:any) => {
    const alunosPorTurma = alunoAulas.map((alunoAula:any) => ({
        id: alunoAula.idAluno,
        nome: alunoAula.nomeAluno,
        presente: alunoAula.presente,
    }));
    const nomeTurma = aulasTurmas.length > 0 ? aulasTurmas[0].nomeTurma : "Sem turma";
    setSelectedAlunosPorTurma(alunosPorTurma);
    setSelectedAlunoAulas(alunoAulas);
    setSelectedTurma(nomeTurma);
    setShowModal(true);
  };

  
  
  const handleCloseModal = () => setShowModal(false);

  const lowerSearch = search.toLowerCase();
  const filteredAulas = Array.isArray(aula) ? aula.filter((aula) => {
    return aula.licao && aula.licao.toLowerCase().includes(lowerSearch);
  }) : [];
  

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentAulas = filteredAulas.slice(
    indexOfFirstItem,
    indexOfLastItem
  );    



 return (
    <>
        {/* Busca + botão */}
        <div className="flex justify-between items-center w-full mb-4 gap-4">

            <input
                className="text-center bg-slate-500 text-white h-10 w-64 rounded-lg px-3 mb-5"
                type="text"
                value={search}
                onChange={event => setSearch(event.target.value)}
                placeholder="Digite lição"
            />

            <a
                href="/aulas/new/0"
                className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shrink-0 mt-12"
                title="NOVO"
            >
                <BsFillPlusCircleFill className="w-8 h-8 text-white" />
            </a>

        </div>

        {/* Tabela */}
        <div className="overflow-x-auto w-full rounded-xl shadow-md">

            <Table
                responsive
                striped
                bordered
                hover
                variant="dark"
                className="min-w-[1400px] mb-0"
            >

                <thead>
                    <tr>
                        <th></th>
                        <th>Trimestre</th>
                        <th>Data</th>
                        <th>Lição</th>
                        <th>Professor</th>
                        <th>Alunos Matriculados</th>
                        <th>Presentes</th>
                        <th>Ausentes</th>
                        <th>Visitantes</th>
                        <th>Total de assistência</th>
                        <th>Bíblias</th>
                        <th>Revistas</th>
                        <th>Oferta</th>
                        <th>Turma</th>
                        <th>Ação</th>
                    </tr>
                </thead>

                <tbody>
                    {currentAulas.map((aula, index) => (
                        <tr key={`${aula.id}-${index}`}>

                            <td>{index + 1}</td>

                            <td>{aula.trimestre}</td>

                            <td>
                                {formatLocalDate(
                                    aula.dia,
                                    "dd/MM/yyyy"
                                )}
                            </td>

                            <td>{aula.licao}</td>

                            <td>
                                {aula.professorAulas.map(
                                    (professor, ProfessorIndex) => (
                                        <div key={ProfessorIndex}>
                                            {professor.nomeProfessor}
                                        </div>
                                    )
                                )}
                            </td>

                            <td>{aula.alunosMatriculados}</td>

                            <td>{aula.presentes}</td>

                            <td>{aula.ausentes}</td>

                            <td>{aula.visitantes}</td>

                            <td>{aula.totalAssistencia}</td>

                            <td>{aula.biblias}</td>

                            <td>{aula.revistas}</td>

                            <td>
                                {aula.oferta.toLocaleString(
                                    'pt-BR',
                                    {
                                        style: 'currency',
                                        currency: 'BRL'
                                    }
                                )}
                            </td>

                            {/* Turmas */}
                            <td>
                                {aula.aulasTurmas &&
                                    aula.aulasTurmas.map(
                                        (turma, turmaIndex) => (
                                            <div
                                                key={`${turma.id}-${turmaIndex}`}
                                                className="cursor-pointer text-blue-300 hover:underline"
                                                onClick={() =>
                                                    handleShowModal(
                                                        aula.aulasTurmas,
                                                        aula.alunoAulas
                                                    )
                                                }
                                            >
                                                {turma.nomeTurma}
                                            </div>
                                        )
                                    )}
                            </td>

                            {/* Ação */}
                            <td>

                                <div className="flex justify-center">

                                    {nomePerfil !== 'professor' ? (

                                        <Link to={`/aulas/${aula.id}`}>

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

                                    ) : (

                                        <button
                                            className="w-10 h-10 flex items-center justify-center"
                                            title="Professor não pode editar aulas"
                                            disabled
                                        >
                                            <BsPencil
                                                className="w-5 h-5"
                                                color="gray"
                                            />
                                        </button>

                                    )}

                                </div>

                            </td>

                        </tr>
                    ))}
                </tbody>

            </Table>

        </div>

        <AlunosModal
            show={showModal}
            handleClose={handleCloseModal}
            alunosPorTurma={selectedAlunosPorTurma}
            alunoAulas={selectedAlunoAulas}
            turma={""}
        />

        {/* Paginação */}
        <div className="flex justify-center mt-4 overflow-x-auto">

            <Pagination>

                <Pagination.Prev
                    onClick={() =>
                        handlePageChange(currentPage - 1)
                    }
                    disabled={currentPage === 1}
                />

                {[...Array(
                    Math.ceil(
                        filteredAulas.length / itemsPerPage
                    )
                )].map((_, pageIndex) => (
                    <Pagination.Item
                        key={`page-${pageIndex}`}
                        active={pageIndex + 1 === currentPage}
                        onClick={() =>
                            handlePageChange(pageIndex + 1)
                        }
                    >
                        {pageIndex + 1}
                    </Pagination.Item>
                ))}

                <Pagination.Next
                    onClick={() =>
                        handlePageChange(currentPage + 1)
                    }
                    disabled={
                        currentPage ===
                        Math.ceil(
                            filteredAulas.length / itemsPerPage
                        )
                    }
                />

            </Pagination>

        </div>
    </>
);

}

export default DataTableAula;