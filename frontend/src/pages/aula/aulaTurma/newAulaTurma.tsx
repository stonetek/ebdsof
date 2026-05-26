/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/no-unused-vars */
import { useNavigate, useParams } from "react-router-dom";
import { Aula } from "../../../types/aula";
import { useEffect, useState } from "react";
import { Turma } from "../../../types/turma";
import { Button } from "react-bootstrap";
import { BiSend } from "react-icons/bi";
import Header from "../../../components/header/header";
import api from "../../../service/api";

function NewAulaTurma() {

    const [id, setId] = useState(null);
    const [licao, setLicao] = useState('');
    const [nomeTurma, setNomeTurma] = useState('');
    const [idAula, setIdAula] = useState(null);
    const [idTurma, setIdTurma] = useState(null);
    const [aula, setAula] = useState<Aula[]>([]);
    const [turma, setTurma] = useState<Turma[]>([]);
    const { aulaTurmaID } = useParams();
    const history = useNavigate();

    async function loadAulaTurma() {
        try {
            const response = await api.get(`/api/aulas/aula-turma-vinculo/${aulaTurmaID}`);
            setId(response.data.id);
            setIdAula(response.data.idAula);
            setLicao(response.data.licao);
            setIdTurma(response.data.idTurma);
            setNomeTurma(response.data.nomeTurma);
        } catch (error) {
            alert('Error while fetching AulaTurma. Try again!');
            history('/aulasEturmas');
        }
    }

    async function loadTurmas() {
        try {
            const response = await api.get('/api/turmas');
            setTurma(response.data);
        } catch (error) {
            console.error('Error fetching turmas:', error);
        }
    }

    async function loadAulas() {
        try {
            const response = await api.get('/api/aulas');
            setAula(response.data);
        } catch (error) {
            console.error('Error fetching Aulas:', error);
        }
    }

    useEffect(() => {
        if (aulaTurmaID !== '0') loadAulaTurma();
        loadTurmas();
        loadAulas();
    }, [aulaTurmaID]);

    async function saveOrUpdate(e: { preventDefault: () => void; }) {
        e.preventDefault();
        const data = {
            idAula: idAula, // Usar aulaId em vez de nomeAula
            idTurma: idTurma, // Usar turmaId em vez de nomeTurma
            id: aulaTurmaID !== '0' ? id : undefined,
        };

        try {
            if (aulaTurmaID === '0') {
                await api.post('/api/aulas/aula-turma-vinculo', data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            } else {
                data.id = id;
                await api.put(`/api/aulas/aula-turma-vinculo/${aulaTurmaID}`, data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            }

            history('/aulasEturmas');
        } catch (error) {
            alert('Essa turma já está vinculada a esta aula. Tente outra Turma ou Aula');
            console.error('Error details:', error);
        }
    }

    const handleAulaChange = (e: { target: { value: any; }; }) => {
        const selectedIdAula = e.target.value;
        const selectedAula = aula.find(aula => aula.id === parseInt(selectedIdAula));
        setIdAula(selectedIdAula);
        setLicao(selectedAula ? selectedAula.licao : '');
    };

    const handleTurmaChange = (e: { target: { value: any; }; }) => {
        const selectedIdTurma = e.target.value;
        const selectedTurma = turma.find(turma => turma.id === parseInt(selectedIdTurma));
        setIdTurma(selectedIdTurma);
        setNomeTurma(selectedTurma ? selectedTurma.nome : '');
    };

    return (
    <>
        <Header />

        <div className="min-h-screen bg-gradient-to-t from-slate-700 flex flex-col items-center px-4 py-8">

            {/* Título */}
            <h1 className="text-3xl md:text-5xl font-bold text-center mb-8">
                {aulaTurmaID === '0' ? 'Add' : 'Update'} AulaTurma
            </h1>

            {/* Formulário */}
            <form
                onSubmit={saveOrUpdate}
                className="bg-gradient-to-t from-zinc-300 w-full max-w-xl rounded-2xl shadow-lg flex flex-col gap-5 p-8"
            >

                {/* Aula */}
                <div className="flex flex-col gap-2">

                    <label
                        htmlFor="licao"
                        className="text-xl font-semibold text-rose-800"
                    >
                        Lição
                    </label>

                    <select
                        id="licao"
                        value={idAula || ''}
                        onChange={handleAulaChange}
                        className="h-10 rounded-lg border border-gray-300 px-3 text-black bg-rose-400"
                    >
                        <option value="">
                            Selecione uma Aula
                        </option>

                        {aula.map(aula => (
                            <option
                                key={aula.id}
                                value={aula.id}
                            >
                                {aula.licao}
                            </option>
                        ))}
                    </select>

                </div>

                {/* Turma */}
                <div className="flex flex-col gap-2">

                    <label
                        htmlFor="NomeTurma"
                        className="text-xl font-semibold text-rose-800"
                    >
                        Nome Turma
                    </label>

                    <select
                        id="NomeTurma"
                        value={idTurma || ''}
                        onChange={handleTurmaChange}
                        className="h-10 rounded-lg border border-gray-300 px-3 text-black bg-rose-400"
                    >
                        <option value="">
                            Selecione uma turma
                        </option>

                        {turma.map(turma => (
                            <option
                                key={turma.id}
                                value={turma.id}
                            >
                                {turma.nome}
                            </option>
                        ))}
                    </select>

                </div>

                {/* Botões */}
                <div className="flex justify-center gap-4 mt-4">

                    <button
                        type="submit"
                        className="flex items-center justify-center gap-2 bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md h-10"
                    >
                        {aulaTurmaID === '0' ? 'Add' : 'Update'}

                        <BiSend
                            title={
                                aulaTurmaID === '0'
                                    ? 'Add'
                                    : 'Update'
                            }
                            className="w-5 h-5"
                        />
                    </button>

                    <Button
                        as="a"
                        href="/aulasEturmas"
                        className="h-10 flex items-center"
                    >
                        VOLTAR
                    </Button>

                </div>

            </form>

        </div>
    </>
);
}

export default NewAulaTurma;