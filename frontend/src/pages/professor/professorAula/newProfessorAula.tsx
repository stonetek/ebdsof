/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useState } from "react";
import { Aula } from "../../../types/aula";
import { Professor } from "../../../types/professor";
import { useNavigate, useParams } from "react-router-dom";
import api from "../../../service/api";
import Header from "../../../components/header/header";
import { Button } from "react-bootstrap";
import { BiSend } from "react-icons/bi";
import footer from "../../../components/footer/footer";

function NewProfessorAula() {

    const [id, setId] = useState(null);
    const [nomeProfessor, setNomeProfessor] = useState('');
    const [licao, setLicao] = useState('');
    const [idProfessor, setIdProfessor] = useState(null);
    const [idAula, setIdAula] = useState(null);
    const [aula, setAula] = useState<Aula[]>([]);
    const [professor, setProfessor] = useState<Professor[]>([]);
    const { professorAulaID } = useParams();
    const history = useNavigate();

    useEffect(() => {
        if (professorAulaID !== '0') {
            loadProfessorAula();
            loadAulas(); // Carregar aulas existentes para edição
        } else {
            loadAulasByTrimestre(); // Carregar aulas com base no trimestre e ano atuais para novo vínculo
        }
        loadProfessores();
    }, [professorAulaID]);



    async function loadProfessorAula() {
        try {
            const response = await api.get(`/api/professores/professor-aula-vinculo/${professorAulaID}`);
            setId(response.data.id);
            setIdProfessor(response.data.idProfessor);
            setNomeProfessor(response.data.nomeProfessor);
            setIdAula(response.data.idAula);
            setLicao(response.data.licao);
        } catch (error) {
            alert('Error while fetching ProfessorTurma. Try again!');
            history('/professorEaulas');
        }
    }

    async function loadAulas() {
        try {
            const response = await api.get('/api/aulas');
            setAula(response.data);
        } catch (error) {
            console.error('Error fetching aulas:', error);
        }
    }

    async function loadAulasByTrimestre() {
        try {
            const today = new Date();
            const ano = today.getFullYear();
            const mes = today.getMonth();

            let trimestre;
            if (mes < 3) {
                trimestre = "1º trimestre";
            } else if (mes < 6) {
                trimestre = "2º trimestre";
            } else if (mes < 9) {
                trimestre = "3º trimestre";
            } else {
                trimestre = "4º trimestre";
            }

            const currentTrimestre = {
                trimestre: trimestre,
                ano: ano
            };

            const response = await api.post('/api/aulas/current-trimestre', currentTrimestre);
            setAula(response.data);
        } catch (error) {
            console.error('Error fetching aulas:', error);
        }
    }

    async function loadProfessores() {
        try {
            const response = await api.get('/api/professores');
            setProfessor(response.data);
        } catch (error) {
            console.error('Error fetching Professors:', error);
        }
    }

    
    async function saveOrUpdate(e: { preventDefault: () => void; }) {
        e.preventDefault();
        const data = {
            idProfessor: idProfessor,
            idAula: idAula,
        };

        try {
            if (professorAulaID === '0') {
                await api.post('/api/professores/professor-aula-vinculo', data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            } else {
                data.id = id;
                await api.put(`/api/professores/professor-aula-vinculo/${professorAulaID}`, data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            }

            history('/professorEaulas');
        } catch (error) {
            alert('Essa turma já está vinculada a esta professor. Tente outra Turma ou Professor');
            console.error('Error details:', error);
        }
    }

    const handleProfessorChange = (e: { target: { value: any; }; }) => {
        const selectedIdProfessor = e.target.value;
        const selectedProfessor = professor.find(professor => professor.id === parseInt(selectedIdProfessor));
        setIdProfessor(selectedIdProfessor);
        setNomeProfessor(selectedProfessor ? selectedProfessor.nome : '');
    };

    const handleTurmaChange = (e: { target: { value: any; }; }) => {
        const selectedIdAula = e.target.value;
        const selectedAula = aula.find(aula => aula.id === parseInt(selectedIdAula));
        setIdAula(selectedIdAula);
        setLicao(selectedAula ? selectedAula.licao : '');
    };

   return (
    <>
        <Header />

        <div className="min-h-screen bg-gradient-to-t from-slate-700 flex flex-col items-center px-4 py-8">

            {/* Título */}
            <h1 className="text-3xl md:text-5xl font-bold text-center mb-8 gap-4">
                {professorAulaID === '0' ? 'Add' : 'Update'} ProfessorAula
            </h1>

            {/* Formulário */}
            <form
                onSubmit={saveOrUpdate}
                className="bg-gradient-to-t from-zinc-300 w-full max-w-xl rounded-2xl shadow-lg flex flex-col gap-5 p-8"
            >

                {/* Professor */}
                <div className="flex flex-col gap-2">

                    <label
                        htmlFor="NomeProfessor"
                        className="text-xl font-semibold text-rose-800"
                    >
                        Nome Professor
                    </label>

                    <select
                        id="NomeProfessor"
                        value={idProfessor || ''}
                        onChange={handleProfessorChange}
                        className="h-10 rounded-lg px-3 text-black bg-red-400 border border-gray-300"
                    >
                        <option value="">
                            Selecione um Professor
                        </option>

                        {professor.map(professor => (
                            <option
                                key={professor.id}
                                value={professor.id}
                            >
                                {professor.nome}
                            </option>
                        ))}

                    </select>

                </div>

                {/* Aula */}
                <div className="flex flex-col gap-2">

                    <label
                        htmlFor="NomeTurma"
                        className="text-xl font-semibold text-rose-800"
                    >
                        Lição
                    </label>

                    <select
                        id="NomeTurma"
                        value={idAula || ''}
                        onChange={handleTurmaChange}
                        className="h-10 rounded-lg px-3 text-black bg-red-400 border border-gray-300"
                    >
                        <option value="">
                            Selecione uma lição
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

                {/* Botões */}
                <div className="flex justify-center gap-4 mt-4 flex-wrap">

                    <button
                        type="submit"
                        className="flex items-center justify-center gap-2 bg-green-500 hover:bg-green-700 text-white font-bold px-4 py-2 rounded-lg shadow-md h-12 transition"
                    >
                        {professorAulaID === '0'
                            ? 'Add'
                            : 'Update'}

                        <BiSend
                            title={
                                professorAulaID === '0'
                                    ? 'Add'
                                    : 'Update'
                            }
                            className="w-5 h-5"
                        />
                    </button>

                    <Button
                        as="a"
                        href="/professorEaulas"
                        className="h-12 flex items-center justify-center px-4 rounded-lg shadow-md bg-blue-500 hover:bg-blue-600 text-white font-semibold"
                    >
                        VOLTAR
                    </Button>

                </div>

            </form>

        </div>

        <footer className="bg-gray-800 text-white py-6 mt-auto">
            <div className="container mx-auto px-4 text-center">
                <p>&copy; 2023 EBD App. Todos os direitos reservados.</p>
            </div>
        </footer>
    </>
);

}

export default NewProfessorAula;