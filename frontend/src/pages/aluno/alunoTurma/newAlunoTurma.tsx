/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useState } from "react";
import { Aluno } from "../../../types/aluno";
import { useNavigate, useParams } from "react-router-dom";
import Header from "../../../components/header/header";
import { BiSend } from "react-icons/bi";
import { Button, Table } from "react-bootstrap";
import { fetchAlunosElegiveis, fetchIgrejaAndEbd, fetchIgrejas, fetchTurmasPorEbd } from "../../../utils/api";
import { EbdTurmas } from "../../../types/ebdTurmas";
import { Igreja } from "../../../types/igreja";
import { IgrejaEbd } from "../../../types/igrejaEbd";
import Footer from "../../../components/footer/footer";
import { BASE_URL } from "../../../utils/requests";



function NewAlunoTurma() {
    const [igrejas, setIgrejas] = useState<Igreja[]>([]);
    const [selectedIgrejaId, setSelectedIgrejaId] = useState<string | null>(null);
    const [ebds, setEbds] = useState<IgrejaEbd[]>([]);
    const [idEbd, setIdEbd] = useState<string | null>(null);
    const [selectedEbdId, setSelectedEbdId] = useState<string | null>(null);
    const [turmas, setTurmas] = useState<EbdTurmas[]>([]);
    const [idTurma, setIdTurma] = useState<string | null>(null);
    const [selectedTurmaId, setSelectedTurmaId] = useState<number | null>(null);
    const [alunoId, setAlunoId] = useState<string | null>(null);
    const [alunos, setAlunos] = useState<Aluno[]>([]);
    const [sexoFilter, setSexoFilter] = useState<string | null>(null);
    const [novoConvertidoFilter, setNovoConvertidoFilter] = useState<boolean | null>(null);
    const { alunoTurmaID } = useParams();
    const acessToken = sessionStorage.getItem('accessToken');
    const history = useNavigate();

    // determine if the current user has an associated church (not admin)
    const igrejaIdSession = sessionStorage.getItem('igrejaId');
    const hasIgreja = !!igrejaIdSession && igrejaIdSession !== 'null';

    useEffect(() => {
        loadIgrejas();
    }, []);

    // helper that loads the ebds for a given church and optionally pre‑selects the first one
    const loadEbdsForIgreja = async (idIgreja: string, autoSelectFirst = false) => {
        setSelectedIgrejaId(idIgreja);
        setIdEbd(null);
        setIdTurma(null);
        setAlunoId(null);
        setEbds([]);
        setAlunos([]);

        try {
            const response = await fetchIgrejaAndEbd(idIgreja);
            setEbds(response.data);

            if (autoSelectFirst && response.data.length > 0) {
                const firstEbdId = String(response.data[0].ebdId);
                setSelectedEbdId(firstEbdId);
                // also populate turmas for the automatically selected ebd
                try {
                    const turmasResp = await fetchTurmasPorEbd(Number(firstEbdId));
                    setTurmas(turmasResp.data);
                } catch (err) {
                    console.error('Error fetching Turmas during auto select:', err);
                }
            }
        } catch (error) {
            console.error('Error fetching EBDs:', error);
        }
    };

    const loadIgrejas = async () => {
        try {
            const response = await fetchIgrejas();
            setIgrejas(response.data);

            // if the user has a church, preselect it and load its EBDs
            if (hasIgreja && igrejaIdSession) {
                const found = response.data.find((i: Igreja) => String(i.id) === String(igrejaIdSession));
                if (found) {
                    await loadEbdsForIgreja(String(found.id), true);
                }
            }
        } catch (error) {
            console.error('Error fetching igrejas:', error);
        }
    };

    const handleIgrejaChange = async (e: { target: { value: any; }; }) => {
        const idIgreja = e.target.value;
        // reuse helper, but do not auto-select an ebd when the user actively changes church
        await loadEbdsForIgreja(idIgreja, false);
    };
    

    const handleEbdChange = async (e: { target: { value: any; }; }) => {
        const idEbd = e.target.value;
        setSelectedEbdId(idEbd);
        setIdTurma(null);
        setAlunoId(null);
        setAlunos([]);
        try {
            const response = await fetchTurmasPorEbd(Number(idEbd));
            setTurmas(response.data);
        }catch (error) {
            console.error('Error fetching Turmas:', error);
        }
    };

    const handleTurmaChange = async (e: { target: { value: any } }) => {
        const idTurma = Number(e.target.value);
        setSelectedTurmaId(idTurma);
        setAlunos([]);
        try {
            const response = await fetchAlunosElegiveis(idTurma);
            const updatedAlunos = response.data.map((aluno: any) => ({
            ...aluno,
            matriculado: aluno.matriculado || (aluno.turmas && aluno.turmas.includes(idTurma)),
            // Normaliza novoConvertido para boolean
            novoConvertido: aluno.novoConvertido === true || aluno.novoConvertido === "true" || aluno.novoConvertido === 1
            }));
            setAlunos(updatedAlunos);
        } catch (error) {
            console.error("Error fetching alunos:", error);
        }
    };
    

    const filteredAlunos = alunos.filter(aluno => {
        if (sexoFilter && aluno.sexo !== sexoFilter) return false;
        if (novoConvertidoFilter !== null && aluno.novoConvertido !== novoConvertidoFilter) return false;
        return true;
    });


    const handleCheckboxChange = (alunoId: number, isChecked : boolean) => {
        setAlunos((prevAlunos: Aluno[]) =>
            prevAlunos.map((aluno: Aluno) =>
                aluno.id === alunoId ? { ...aluno, matriculado: isChecked  } : aluno
            )
        );
    };
    
    async function saveOrUpdate(e: { preventDefault: () => void; }) {
        e.preventDefault();
        const alunosToUpdate = alunos
            .filter(aluno => aluno.matriculado)  // Filtrar apenas os alunos selecionados
            .map(aluno => aluno.id);             // Obter apenas os IDs dos alunos
        
        const payload = {
            alunoIds: alunosToUpdate,  // Lista de IDs de alunos
            turmaId: selectedTurmaId   // ID da turma
        };
        console.log('Payload a ser enviado para /alunos/aluno-turma-vinculo:', payload);
        try {
            if (alunosToUpdate.length > 0) {
                const response = await fetch(`${BASE_URL}/api/alunos/aluno-turma-vinculo`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization' : `Bearer ${acessToken}`
                    },
                    body: JSON.stringify(payload) // Enviar o payload no corpo da requisição
                });
                
                if (!response.ok) {
                    throw new Error('Failed to save data');
                }
                
                history('/alunosEturmas');  // Redirecionar após o sucesso
            }
        } catch (error) {
            alert('Error while recording AlunoTurma. Try again!');
            console.error('Error details:', error);
        }
    }
    

    

    return (
        <>
            <Header />
            <div className="w-full min-h-screen bg-gradient-to-t from-slate-700 flex flex-col justify-center items-center p-4 py-10">
                <h1 className="text-3xl sm:text-4xl md:text-5xl mt-20 mb-10 text-center text-black">{alunoTurmaID === '0' ? 'Add' : 'Update'} AlunoTurma</h1>
                <form onSubmit={saveOrUpdate} className="bg-gradient-to-t from-zinc-300 w-full sm:w-5/6 md:w-3/4 lg:w-3/6 max-w-2xl flex flex-col items-center justify-center gap-3 p-4 md:p-6 rounded-lg">
                    {/* Select para Igrejas */}
                    <label htmlFor="igreja" className="text-lg sm:text-xl md:text-2xl text-rose-800">Selecione a Igreja</label>
                    <select 
                        id="igreja" 
                        value={selectedIgrejaId || ''} 
                        onChange={handleIgrejaChange}
                        className="w-full sm:w-60 text-black bg-red-400 p-2 rounded"
                        disabled={hasIgreja}
                    >
                        <option value="">Selecione uma igreja</option>
                        {igrejas.map((igreja) => (
                            <option key={igreja.id} value={igreja.id}>{igreja.nome}</option>
                        ))}
                    </select>

                    {/* Seletor de EBD */}
                    <label htmlFor="EbdSelect" className="text-lg sm:text-xl md:text-2xl text-rose-800 mt-4">EBD</label>
                    <select
                        id="EbdSelect"
                        value={selectedEbdId || ''}
                        onChange={handleEbdChange}
                        className="w-full sm:w-60 text-black bg-red-400 p-2 rounded"
                        disabled={!selectedIgrejaId}
                    >
                        <option value="">Selecione uma EBD</option>
                        {ebds && ebds.length > 0 && ebds.map(ebd => (
                            <option key={ebd.ebdId} value={ebd.ebdId}>{ebd.nomeEbd}</option>
                        ))}
                    </select>

                    {/* Seletor de Turma baseado na EBD selecionada */}
                    <label htmlFor="NomeTurma" className="text-lg sm:text-xl md:text-2xl text-rose-800 mt-4">Nome Turma</label>
                    <select
                        id="NomeTurma"
                        value={selectedTurmaId || ''}
                        onChange={handleTurmaChange}
                        className="w-full sm:w-60 text-black bg-red-400 p-2 rounded"
                        disabled={!selectedEbdId}
                    >
                        <option value="">Selecione uma turma</option>
                        {turmas && turmas.length > 0 && turmas.map(turma => (
                            <option key={turma.idTurma} value={turma.idTurma}>{turma.nomeTurma}</option>
                        ))}
                    </select>

                    <div className="flex flex-col w-full gap-4 mt-4">

                        {/* Radio buttons para filtrar por sexo */}
                        <div className="flex flex-col sm:flex-row gap-4 sm:gap-5">
                            <label className="text-lg sm:text-xl md:text-2xl text-rose-800 flex-shrink-0">Gênero:</label>
                            
                            <div className="flex gap-4 flex-wrap">
                            
                                <label className="flex items-center">
                                    <input
                                        type="radio"
                                        value="M"
                                        checked={sexoFilter === 'M'}
                                        onChange={() => setSexoFilter('M')}
                                    />
                                    <span className="ml-2">Masculino</span>
                                </label>
                                <label className="flex items-center">
                                    <input
                                        type="radio"
                                        value="F"
                                        checked={sexoFilter === 'F'}
                                        onChange={() => setSexoFilter('F')}
                                    />
                                    <span className="ml-2">Feminino</span>
                                </label>

                            </div>
                        </div>

                        {/* Radio buttons para filtrar por novo convertido */}
                       <div className="flex flex-col sm:flex-row gap-4 sm:gap-5">
                            <label className="text-lg sm:text-xl md:text-2xl text-rose-800 flex-shrink-0">Novo Convertido:</label>
                            
                            <div className="flex gap-4 flex-wrap">
                                <label className="flex items-center">
                                    <input
                                    type="radio"
                                    checked={novoConvertidoFilter === true}
                                    onChange={() => setNovoConvertidoFilter(true)}
                                    />
                                    <span className="ml-2">Sim</span>
                                </label>
                                
                                <label className="flex items-center">
                                    <input
                                    type="radio"
                                    checked={novoConvertidoFilter === false}
                                    onChange={() => setNovoConvertidoFilter(false)}
                                    />
                                    <span className="ml-2">Não</span>
                                </label>
                                
                                <label className="flex items-center">
                                    <input
                                    type="radio"
                                    checked={novoConvertidoFilter === null}
                                    onChange={() => setNovoConvertidoFilter(null)}
                                    />
                                    <span className="ml-2">Todos</span>
                                </label>
                        </div>

                    </div>    
                    </div>

                    <div className="overflow-x-auto w-full mt-4">
                    <Table striped bordered hover variant="dark" className="min-w-full">
                        <thead>
                            <tr>
                                <th className="py-2 px-2 sm:px-4 border text-xs sm:text-sm">Nome do Aluno</th>
                                <th className="py-2 px-2 sm:px-4 border text-xs sm:text-sm">Matriculado</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filteredAlunos.length > 0 ? (
                                filteredAlunos.map(aluno => (
                                    <tr key={aluno.id}>
                                        <td className="py-2 px-2 sm:px-4 border text-xs sm:text-sm">{aluno.nome}</td>
                                        <td className="py-2 px-2 sm:px-4 border">
                                            {aluno.matriculado ? (
                                                <div className="flex items-center gap-2">
                                                    <span className="text-xs sm:text-sm">SIM</span>
                                                    <input
                                                        type="checkbox"
                                                        checked={true}
                                                        disabled
                                                    />
                                                </div>
                                            ) : (
                                                <div className="flex items-center gap-2">
                                                    <span className="text-xs sm:text-sm">{aluno.matriculado ? 'SIM' : 'NÃO'}</span>
                                                    <input
                                                        type="checkbox"
                                                        checked={aluno.matriculado || false}
                                                        onChange={(e) => handleCheckboxChange(aluno.id, e.target.checked)}
                                                    />
                                                </div>
                                            )}
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan={2} className="text-center py-4">Nenhum aluno encontrado</td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                    </div>

                    <div className="flex flex-row sm:flex-row text-center gap-4 mt-6 w-full justify-center">
                        <button type="submit" className="flex items-center justify-center gap-2 bg-green-500 hover:bg-blue-600 text-white px-4 py-2 rounded">
                            {alunoTurmaID === '0' ? 'Add' : 'Update'}
                            <BiSend title={alunoTurmaID === '0' ? 'Add' : 'Update'} color="white" className="w-5 h-5" />
                        </button>
                        <Button as="a" href="/alunosEturmas">VOLTAR</Button>
                    </div>        
                </form>
            <footer className="w-screen mt-10">
                <Footer/>
            </footer>
            </div>

        </>
    );
}

export default NewAlunoTurma;