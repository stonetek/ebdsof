import { useState, useEffect } from "react";
import { fetchTurmas, fetchAlunosPorFaixaEtaria } from "../../utils/api";
import Header from "../../components/header/header";
import { Button } from "react-bootstrap";
import axios from "axios";
import Footer from "../../components/footer/footer";
import api from "../../service/api";
import { BASE_URL } from "../../utils/requests";

interface Turma {
    id: number;
    nome: string;
    idadeMinima: number;
    idadeMaxima: number;
}

interface Aluno {
    id: number;
    nome: string;
    aniversario: string; // or Date depending on your API
}


const Matricula = () => {
    const [turmas, setTurmas] = useState<Turma[]>([]);
    const [alunos, setAlunos] = useState<Aluno[]>([]);
    const [selectedTurma, setSelectedTurma] = useState<string>('');
    const [selectedAlunos, setSelectedAlunos] = useState<number[]>([]);

    useEffect(() => {
        const loadTurmas = async () => {
            try {
                const response = await fetchTurmas();
                setTurmas(response.data);
            } catch (error) {
                console.error('Failed to fetch turmas', error);
            }
        };

        loadTurmas();
    }, []);

    const handleTurmaChange = async (event: React.ChangeEvent<HTMLSelectElement>) => {
        const nomeTurma = event.target.value;
        setSelectedTurma(nomeTurma);
        try {
            const response = await fetchAlunosPorFaixaEtaria(nomeTurma);
            setAlunos(response.data);
        } catch (error) {
            console.error('Failed to fetch alunos', error);
        }
    };

    const handleAlunoSelect = (alunoId: number) => {
        setSelectedAlunos((prevSelected) =>
            prevSelected.includes(alunoId)
                ? prevSelected.filter((id) => id !== alunoId)
                : [...prevSelected, alunoId]
        );
    };

    type MatriculaData = {
        turmaId: number;
        alunoId?: number;
        alunosIds?: number[];
    };

    const matricularAlunos = async (turmaId: number, alunoId: number | null, alunosIds: number[] | undefined) => {
        try {
            const data: MatriculaData = {
                turmaId: turmaId
            };
    
            if (alunoId !== null) {
                data.alunoId = alunoId;
            }
    
            if (alunosIds && alunosIds.length > 0) {
                data.alunosIds = alunosIds;
            }
    
            await api.post(`${BASE_URL}/api/turmas/matricular-aluno`, data);
        } catch (error) {
            console.error('Failed to matriculate alunos', error);
            throw error;
        }
    };
    

    const handleMatricular = async () => {
        if (selectedTurma && selectedAlunos.length > 0) {
            try {
                const turma = turmas.find((turma) => turma.nome === selectedTurma);
                if (turma) {
                    await matricularAlunos(turma.id, null, selectedAlunos);
                    alert('Aluno(s) matriculado(s) com sucesso!');
                    setAlunos([]);
                    setSelectedAlunos([]);
                    setSelectedTurma('');
                }
            } catch (error) {
                console.error('Failed to matriculate alunos', error);
                alert('Falha ao matricular; Aluno(s) já está(ão) matriculado(s) nessa classe.');
            }
        } else {
            alert('Selecione uma turma e ao menos um aluno');
        }
    };
    
    return (
        <>
            <Header/>
            <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
                <h1 className="text-2xl font-bold mb-6">Matricula de Alunos</h1>
                <div className="mb-4">
                    <label htmlFor="turmaSelect" className="mr-2">Selecione uma Turma: </label>
                    <select
                        id="turmaSelect"
                        value={selectedTurma}
                        onChange={handleTurmaChange}
                        className="p-2 border border-gray-300 rounded"
                    >
                        <option value="">--Selecione uma Turma--</option>
                        {turmas.map((turma) => (
                            <option key={turma.id} value={turma.nome}>
                                {turma.nome}
                            </option>
                        ))}
                    </select>
                </div>

                {alunos.length > 0 ? (
                    <div className="w-full max-w-2xl">
                        <h2 className="text-xl font-semibold mb-4">Alunos Elegíveis</h2>
                        <table className="w-full table-auto border-collapse border border-gray-300">
                            <thead>
                                <tr>
                                    <th className="px-4 py-2 border border-gray-300">Nome</th>
                                    <th className="px-4 py-2 border border-gray-300">Selecionar</th>
                                </tr>
                            </thead>
                            <tbody>
                                {alunos.map((aluno) => (
                                    <tr key={aluno.id}>
                                        <td className="px-4 py-2 border border-gray-300">{aluno.nome}</td>
                                        <td className="px-4 py-2 border border-gray-300 text-center">
                                            <input
                                                type="checkbox"
                                                checked={selectedAlunos.includes(aluno.id)}
                                                onChange={() => handleAlunoSelect(aluno.id)}
                                            />
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                        <button
                            onClick={handleMatricular}
                            className="ml-20 mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                        >
                            Matricular Alunos
                        </button>
                        <Button className="ml-20 px-4 py-2" as="a" href="/home">VOLTAR</Button>
                    </div>
                ) : (
                    <Button className="ml-20 px-4 py-2" as="a" href="/home">VOLTAR</Button>
                )}
            </div>

            <footer>
                <Footer />
            </footer>

        </>
    );
};

export default Matricula;