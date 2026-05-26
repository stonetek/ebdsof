import { useEffect, useState } from "react";
import { fetchAlunosByTurma, fetchAulasByTurma, fetchEbdAndTurmasByIgreja } from "../../../utils/api";
import { useParams } from "react-router-dom";
import Header from "../../header/header";
import { TfiSearch } from "react-icons/tfi";
import { Button, Pagination, Table } from "react-bootstrap";

interface Turma {
    id: number;
    nomeTurma: string;
}

interface IgrejaEbdTurmaDTO {
    idEbd: number;
    ebdNome: string;
    turmas: Turma[];
}

interface Aluno {
    id: number;
    nome: string;
}

interface Aula {
    id: number;
    licao: string;
    nomeTurma: string;
}


function IgrejaEbdTurmaFind() {

    const { id } = useParams<{ id: string }>();
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage] = useState(5);
    const [search, setSearch] = useState('');
    const [igrejaEbdTurmas, setIgrejaEbdTurmas] = useState<IgrejaEbdTurmaDTO | null>(null);
    const [alunos, setAlunos] = useState<Aluno[]>([]);
    const [aulas, setAulas] = useState<Aula[]>([]);

    

    useEffect(() => {
        fetchEbdAndTurmasByIgreja(Number(id))
            .then(response => {
                if (response.data && Array.isArray(response.data.turmas)) {
            setIgrejaEbdTurmas(response.data);
                } else {
                    console.error("Resposta da API não contém um array de turmas", response.data);
                }
            })
            .catch(error => console.log(error));
    }, [id]);


    const fetchAlunos = async (idTurma: number) => {
        try {
            const response = await fetchAlunosByTurma(idTurma);
            if (Array.isArray(response.data)) {
                setAlunos(response.data);
            } else {
                console.error("Response data is not an array:", response.data);
            }
        } catch (error) {
            console.error("Error fetching students:", error);
        }
    };
    
    
    const fetchAulas = async (idTurma: number) => {
        try {
            const response = await fetchAulasByTurma(idTurma);
            if ( Array.isArray(response.data)) {
                setAulas(response.data);
            } else {
                console.error("Response data is not an array:", response.data);
            }
        } catch (error) {
            console.error("Error fetching lessons:", error);
        }
    };

    const lowerSearch = search.toLowerCase();
    const filteredTurmas = igrejaEbdTurmas?.turmas.filter((turma) => turma.nomeTurma.toLowerCase().includes(lowerSearch)) || [];

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentTurmas = filteredTurmas.slice(indexOfFirstItem, indexOfLastItem);


    const handlePageChange = (page: number) => {
        setCurrentPage(page);
    };

    return (
        <>
            <div className="min-h-screen bg-white text-black">
                <Header />
                <div className="flex w-1/3 mt-20 mb-5 ml-5 rounded-3xl">
                    <input
                    className="w-2/3 mr-2 text-center bg-slate-500"
                    type="text"
                    value={search}
                    onChange={event => setSearch(event.target.value)}
                    placeholder="digite nome"
                    />
                    <TfiSearch className="mt-1 w-9 h-10" />

                </div>
            
                {igrejaEbdTurmas && (
                    <Table striped bordered hover variant="dark">
                        <thead>
                            <tr>
                                <th></th>
                                <th>Nome EBD</th>
                                <th>Turma</th>
                                <th className="flex justify-center">Ação</th>
                            </tr>
                        </thead>
                        <tbody>
                            {currentTurmas.length > 0 ? (
                                currentTurmas.map((turma, index) => (
                                    <tr key={turma.id}>
                                        <td>{index + 1}</td>
                                        <td>{igrejaEbdTurmas.ebdNome}</td>
                                        <td>{turma.nomeTurma}</td>
                                        <td className="flex justify-center gap-4">
                                        <Button onClick={() => fetchAlunos(turma.id)}>Ver Alunos</Button>
                                        <Button onClick={() => fetchAulas(turma.id)}>Ver Aulas</Button>
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan={4} className="text-center">
                                        {igrejaEbdTurmas.ebdNome} - Sem turmas disponíveis
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                )}

                {igrejaEbdTurmas && currentTurmas.length > 0 && (
                    <Pagination className="justify-content-center">
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
                            disabled={currentPage === Math.ceil(filteredTurmas.length / itemsPerPage)}
                        />
                    </Pagination>
                )}
                <div className="flex justify-center">
                <Button as="a" href="/igrejas" className="mt-2">VOLTAR</Button>
                </div>

                 {/* Display fetched alunos and aulas */}
        <div className="flex justify-center mt-2 gap-96">
          {alunos.length > 0 && (
            <div>
              <h3>Alunos:</h3>
              <Table striped bordered hover variant="dark">
                <thead>
                  <tr>
                    <th></th>
                    <th>Nome</th>
                  </tr>
                </thead>
                <tbody>
                  {alunos.map((aluno, index) => (
                    <tr key={aluno.id}>
                      <td>{index + 1}</td>
                      <td>{aluno.nome}</td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </div>
          )}

          {aulas.length > 0 && (
            <div>
              <h3>Aulas:</h3>
              <Table striped bordered hover variant="dark">
                <thead>
                  <tr>
                    <th></th>
                    <th>Lição</th>
                    <th>Nome Turma</th>
                  </tr>
                </thead>
                <tbody>
                  {aulas.map((aula, index) => (
                    <tr key={aula.id}>
                      <td>{index + 1}</td>
                      <td>{aula.licao}</td>
                      <td>{aula.nomeTurma}</td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </div>
          )}
                </div>
            </div>    
        </>
    )
}

export default IgrejaEbdTurmaFind;