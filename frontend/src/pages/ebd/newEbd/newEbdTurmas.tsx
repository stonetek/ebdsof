/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/no-unused-vars */
import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Header from '../../../components/header/header';
import api from '../../../service/api';
import { BiSend } from 'react-icons/bi';
import { Button } from 'react-bootstrap';
import { Ebd } from '../../../types/ebd';
//import { fetchEbds, fetchTurmasPorEbd } from '../../../utils/api';
//import { EbdTurmas } from '../../../types/ebdTurmas';
import { Turma } from '../../../types/turma';
import Footer from '../../../components/footer/footer';



function NewEbdTurmas() {
    const [id, setId] = useState(null);
    const [ebdId, setEbdId] = useState(null);
    const [turmaId, setTurmaId] = useState(null);
    const [nomeTurma, setNomeTurma] = useState('');
    const [nomeEbd, setNomeEbd] = useState('');
    const [turma, setTurma] = useState<Turma[]>([]);
    const [ebd, setEbd] = useState<Ebd[]>([]);
    const { ebdTurmaID } = useParams();
    const [erro, setErro] = useState("");
    const history = useNavigate();

    async function loadEbdTurma() {
        try {
            const response = await api.get(`/api/escolabiblica/ebd-turma-vinculo/${ebdTurmaID}`);
            setId(response.data.id);
            setTurmaId(response.data.turmaId);
            setNomeTurma(response.data.nomeTurma);
            setEbdId(response.data.ebdId);
            setNomeEbd(response.data.nomeEbd);
        } catch (error) {
            alert('Error while fetching EbdTurma. Try again!');
            history('/escolabiblicaEClasses');
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

    async function loadEbds() {
        try {
            const response = await api.get('/api/escolabiblica');
            setEbd(response.data);
        } catch (error) {
            console.error('Error fetching ebds:', error);
        }
    }

    useEffect(() => {
        if (ebdTurmaID !== '0') loadEbdTurma();
        loadTurmas();
        loadEbds();
    }, [ebdTurmaID]);

    async function saveOrUpdate(e: { preventDefault: () => void; }) {
        e.preventDefault();
        const data = {
            ebdId: ebdId,
            turmaId: turmaId,
        };

        try {
            if (ebdTurmaID === '0') {
                await api.post('/api/escolabiblica/ebd-turma-vinculo', data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            } else {
                data.id = id;
                await api.put(`/api/escolabiblica/ebd-turma-vinculo/${ebdTurmaID}`, data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            }

            history('/escolabiblicaEClasses');
        } catch (error) {
            alert('Essa turma já está vinculada a esta ebd');
            console.error('Error details:', error);
        }
    }

    const handleEbdChange = (e: { target: { value: any; }; }) => {
        const selectedEbdId = e.target.value;
        const selectedEbd = ebd.find(ebd => ebd.id === parseInt(selectedEbdId));
        setEbdId(selectedEbdId);
        setNomeEbd(selectedEbd ? selectedEbd.nome : '');
    };

    const handleTurmaChange = (e: { target: { value: any; }; }) => {
        const selectedTurmaId = e.target.value;
        const selectedTurma = turma.find(turma => turma.id === parseInt(selectedTurmaId));
        setTurmaId(selectedTurmaId);
        setNomeTurma(selectedTurma ? selectedTurma.nome : '');
    };


   return (
        <>
            <Header />
            <div className="w-screen min-h-screen bg-gradient-to-t from-slate-700 flex flex-col items-center pt-8 mt-5">
            <h1 className="text-3xl mb-6">
                {ebdTurmaID === '0' ? 'Add' : 'Update'} EbdTurma
            </h1>

            <form
                onSubmit={saveOrUpdate}
                className="bg-gradient-to-t from-zinc-300 w-3/4 h-3/6 flex flex-col items-center justify-center gap-3 p-6 rounded-lg"
            >
                <label htmlFor="NomeEbd" className="text-2xl text-rose-800">Nome Ebd</label>
                <select
                id="NomeEbd"
                value={ebdId || ''}
                onChange={handleEbdChange}
                className="w-60 text-black bg-red-400"
                >
                <option value="">Selecione uma EBD</option>
                {ebd.map(ebd => (
                    <option key={ebd.id} value={ebd.id}>{ebd.nome}</option>
                ))}
                </select>

                <label htmlFor="NomeTurma" className="text-2xl text-rose-800">Nome Turma</label>
                <select
                id="NomeTurma"
                value={turmaId || ''}
                onChange={handleTurmaChange}
                className="w-60 text-black bg-red-400"
                >
                <option value="">Selecione uma turma</option>
                {turma.map(turma => (
                    <option key={turma.id} value={turma.id}>{turma.nome}</option>
                ))}
                </select>

                {erro && <p className="text-red-700">{erro}</p>}

                <div className="flex flex-row items-center justify-center gap-6 mt-8 mb-5">
                <button
                    type="submit"
                    className="flex flex-row items-center justify-center w-24 h-12 bg-green-500 hover:bg-green-600 text-white rounded-lg shadow-md gap-2"
                >
                    {ebdTurmaID === '0' ? 'Add' : 'Update'}
                    <BiSend title={ebdTurmaID === '0' ? 'Add' : 'Update'} color="white" className="w-8 h-8" />
                </button>

                <Button
                    className="w-24 h-12 flex items-center justify-center rounded-lg shadow-md bg-blue-500 hover:bg-blue-600 text-white font-semibold"
                    as="a"
                    href="/escolabiblicaEClasses"
                >
                    VOLTAR
                </Button>
                </div>
            </form>
            </div>
            <footer>
            <Footer />
            </footer>
        </>
    );

}

export default NewEbdTurmas;
