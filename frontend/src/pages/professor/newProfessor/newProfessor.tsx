/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../../../service/api";
import Header from "../../../components/header/header";
import { BiSend } from "react-icons/bi";
import { EbdTurmas } from "../../../types/ebdTurmas";
import { Button } from "react-bootstrap";



function NewProfessor() {
    
    const [ id, setId] = useState(null);
    const [ nome, setNome] = useState('');
    const [ aniversario, setAniversario] = useState('');
    const [igrejaId] = useState<number | null>(() => {
        const storedIgrejaId = sessionStorage.getItem('igrejaId');
        if (!storedIgrejaId || storedIgrejaId === 'null' || storedIgrejaId === 'undefined') return null;
        const parsed = Number(storedIgrejaId);
        return Number.isNaN(parsed) ? null : parsed;
    });
    const [turmas, setTurmas] = useState<EbdTurmas[]>([]);
    const [turmaSelecionada, setTurmaSelecionada] = useState<number | "">("");

    const {professorID} = useParams();
    const history = useNavigate();

    async function loadProfessor() {
        try {
            const response = await api.get(`/api/professores/${professorID}`)
            const formatLocalDate1 = response.data.aniversario.split("T", 10)[0];
            setId(response.data.id);
            setNome(response.data.nome);
            setAniversario(formatLocalDate1)
            
        } catch (error) {
            alert('Error recovering professor" Try again!');
            history('/professores')
        }
    }

    useEffect(() => {
        if (professorID === '0') return;
        else loadProfessor();
    }, [professorID])

    useEffect(() => {
        if (igrejaId) {
            loadTurmas();
        }
    }, [igrejaId]);

    async function loadTurmas() {
        if (!igrejaId) return;
    
        try {
            const response = await api.get(`/api/igrejas/${igrejaId}/ebd-turmas`);
            // Acessa o array de turmas dentro da resposta
            const turmasArray = Array.isArray(response.data.turmas) ? response.data.turmas : [];
            setTurmas(turmasArray);
        } catch (error) {
            alert('Error loading turmas. Try again!');
        }
    }
    

    async function saveOrUpdate(e:{ preventDefault: () => void; }) {
        e.preventDefault();
        
        const data: { nome: string; aniversario?: string; igrejaId?: number; turmaId?: number; id?: number | null } = {
            nome,
            ...(aniversario ? { aniversario} : {}),
            ...(igrejaId ? { igrejaId } : {}),
            ...(igrejaId && turmaSelecionada ? { turmaId: turmaSelecionada } : {}),
        };
    
        try {
            if (professorID === '0') {
                await api.post('/api/professores', data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            } else {
                data.id = id;
                await api.put(`/api/professores/${professorID}`, data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            }

            history('/professores')
        } catch (error) {
            alert('Error while recording professor Try again!')
        }       
    }

   

    return (
    <>
        <Header />

        <div className="min-h-screen bg-gradient-to-t from-slate-700 flex flex-col items-center px-4 py-8">

            {/* Título */}
            <h1 className="text-3xl md:text-5xl font-bold text-center mb-8">
                {professorID === '0' ? 'Add' : 'Update'} Professor
            </h1>

            {/* Formulário */}
            <form
                key={professorID}
                onSubmit={saveOrUpdate}
                className="bg-gradient-to-t from-zinc-300 w-full max-w-xl rounded-2xl shadow-lg flex flex-col gap-5 p-8"
            >

                {/* Nome */}
                <div className="flex flex-col gap-2">

                    <label
                        htmlFor="Nome"
                        className="text-xl font-semibold text-rose-800"
                    >
                        Nome
                    </label>

                    <input
                        type="text"
                        value={nome}
                        onChange={e => setNome(e.target.value)}
                        className="h-10 rounded-lg border border-gray-300 px-3 text-black bg-rose-400"
                    />

                </div>

                {/* Aniversário */}
                <div className="flex flex-col gap-2">

                    <label
                        htmlFor="Niver"
                        className="text-xl font-semibold text-rose-800"
                    >
                        Aniversário
                    </label>

                    <input
                        type="date"
                        value={aniversario}
                        onChange={e => setAniversario(e.target.value)}
                        className="h-10 rounded-lg border border-gray-300 px-3 text-black bg-rose-400"
                    />

                </div>

                {/* Turma */}
                {igrejaId && (
                    <div className="flex flex-col gap-2">

                        <label
                            htmlFor="Turma"
                            className="text-xl font-semibold text-rose-800"
                        >
                            Turma
                        </label>

                        <select
                            value={turmaSelecionada ?? ''}
                            onChange={e => {
                                const val = Number(e.target.value);

                                setTurmaSelecionada(
                                    Number.isNaN(val)
                                        ? ""
                                        : val
                                );
                            }}
                            className="h-10 rounded-lg border border-gray-300 px-3 text-black bg-rose-400"
                        >

                            <option value="">
                                Selecione uma turma
                            </option>

                            {turmas.map(turma => (
                                <option
                                    key={turma.id}
                                    value={turma.id}
                                >
                                    {turma.nomeTurma}
                                </option>
                            ))}

                        </select>

                    </div>
                )}

                {/* Botões */}
                <div className="flex justify-center gap-4 mt-4">

                    <button
                        type="submit"
                        className="flex items-center justify-center gap-2 bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md h-10"
                    >
                        {professorID === '0' ? 'Add' : 'Update'}

                        <BiSend
                            title="Adicionar"
                            className="w-5 h-5"
                        />
                    </button>

                    <Button
                        variant="primary"
                        className="h-10 flex items-center"
                        as="a"
                        href="/professores"
                    >
                        VOLTAR
                    </Button>

                </div>

            </form>

        </div>
    </>
)
}

export default NewProfessor;