/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/no-explicit-any */
import { useState, useEffect } from "react";
import { Button } from "react-bootstrap";
import { BiSend } from "react-icons/bi";
import { useParams, useNavigate } from "react-router-dom";
import Header from "../../../components/header/header";
import api from "../../../service/api";
import { Aluno } from "../../../types/aluno";
import { Turma } from "../../../types/turma";
import { fetchTurmas, fetchAlunos } from "../../../utils/api";
import Footer from "../../../components/footer/footer";

function EditAlunoTurma() {

    const [id, setId] = useState(null);
    const [turmas, setTurmas] = useState<Turma[]>([]);
    const [turmaId, setTurmaId] = useState(null);
    const [alunos, setAlunos] = useState<Aluno[]>([]);
    const [alunoId, setAlunoId] = useState(null);
    const { alunoTurmaID } = useParams();
    const history = useNavigate();


    async function loadAlunoTurma() {
        try {
            const response = await api.get(`/api/alunos/aluno-turma-vinculo/${alunoTurmaID}`);
            setId(response.data.id);
            setAlunoId(response.data.alunoId);
            setTurmaId(response.data.turmaId);
        } catch (error) {
            alert('Error while fetching EbdTurma. Try again!');
            history('/alunos');
        }
    }

    async function loadTurmas() {
        try {
            const response = await fetchTurmas();
            setTurmas(response.data);
        } catch (error) {
            console.error('Error fetching turmas:', error);
        }
    }
    
    async function loadAlunos() {
        try {
            const response = await fetchAlunos();
            setAlunos(response.data);
        } catch (error) {
            console.error('Error fetching alunos:', error);
        }
    }



    useEffect(() => {
        if (alunoTurmaID !== '0') loadAlunoTurma();
        loadTurmas();
        loadAlunos();
    }, [alunoTurmaID]);

    const handleTurmaChange = async (e: { target: { value: any; }; }) => {
        const turmaId =  e.target.value;
        setTurmaId(turmaId);
    };

    const handleAlunoChange = async (e: { target: { value: any; }; }) => {
        const alunoId =  e.target.value;
        setAlunoId(alunoId);
    };

    async function saveOrUpdate(e: { preventDefault: () => void; }) {
        e.preventDefault();
        const data = {
            alunoId: alunoId,
            turmaId: turmaId,
        };
        try {
            await api.put(`/api/alunos/aluno-turma-vinculo/${alunoTurmaID}`, data, {
                    headers: {
                        'Content-Type': 'application/json'
                },
            });

            history('/alunosEturmas');
        } catch (error) {
            alert('Error while recording igreja. Try again!');
            console.error('Error details:', error);
        }
    }

    return (
        <>
            <Header />
            <div className="w-full min-h-screen bg-gradient-to-t from-slate-700 flex flex-col justify-center items-center mt-10 p-4">
                <h1 className="text-3xl sm:text-4xl md:text-5xl mb-10 text-center">{alunoTurmaID === '0' ? 'Add' : 'Update'} AlunoTurma</h1>
                <form onSubmit={saveOrUpdate} className="bg-gradient-to-t from-zinc-300 w-full sm:w-5/6 md:w-3/4 lg:w-3/6 max-w-2xl flex flex-col items-center justify-center gap-3 p-4 md:p-6">
                    <label htmlFor="NomeTurma" className="text-2xl text-rose-800">Nome Turma</label>
                    <select
                        id="NomeTurma"
                        value={turmaId || ''}
                        onChange={handleTurmaChange}
                        className="w-60 text-black bg-red-400"
                    >
                        <option value="">Selecione uma turma</option>
                        {turmas.map(turma => (
                            <option key={turma.id} value={turma.id}>{turma.nome}</option>
                        ))}
                    </select>
                    <label htmlFor="NomeAluno" className="text-2xl text-rose-800">Nome Aluno</label>
                    <select
                        id="NomeAluno"
                        value={alunoId || ''}
                        onChange={handleAlunoChange}
                        className="w-60 text-black bg-red-400"
                    >
                        <option value="">Selecione um aluno</option>
                        {alunos.map(aluno => (
                            <option key={aluno.id} value={aluno.id}>{aluno.nome}</option>
                        ))}
                    </select>
                    <button type="submit" className="flex items-center justify-center">
                        {alunoTurmaID === '0' ? 'Add' : 'Update'}
                        <BiSend title={alunoTurmaID === '0' ? 'Add' : 'Update'} color="green" className="w-20 h-12" />
                    </button>
                    <Button as="a" href="/alunosEturmas">VOLTAR</Button>
                </form>
                <footer className="w-screen">
                    <Footer/>
                </footer>
            </div>

        </>
    );
}

export default EditAlunoTurma;