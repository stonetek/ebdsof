/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable react-hooks/exhaustive-deps */
import { SetStateAction, useEffect, useState } from "react";
import { BiSend } from "react-icons/bi";
import { useParams, useNavigate} from "react-router-dom";
import api from '../../../service/api'
import Header from "../../../components/header/header";
import { Aluno } from "../../../types/aluno";
import { Turma } from "../../../types/turma";
import { fetchTurmas, fetchTurmasPorIgreja } from "../../../utils/api";
import Footer from "../../../components/footer/footer";
import { Professor } from "../../../types/professor";
import { Button } from "react-bootstrap";



function NewAula() {
    const [id, setId] = useState(null);
    const [trimestre, setTrimestre] = useState('');
    const [dia, setDia] = useState('');
    const [licao, setLicao] = useState('');
    const [alunosMatriculados, setAlunosMatriculados] = useState('');
    const [visitantes, setVisitantes] = useState('');
    const [presentes, setPresentes] = useState('');
    const [ausentes, setAusentes] = useState('');
    const [totalAssistencia, setTotalAssistencia] = useState('');
    const [biblias, setBiblias] = useState('');
    const [revistas, setRevistas] = useState('');
    const [oferta, setOferta] = useState('');
    const [alunos, setAlunos] = useState<Aluno[]>([]);
    const [turmas, setTurmas] = useState<Turma[]>([]);
    const [turmaSelecionada, setTurmaSelecionada] = useState('');
    const [isEditMode, setIsEditMode] = useState(false);
    const [professores, setProfessores] = useState<Professor[]>([]);
    const [professorSelecionado, setProfessorSelecionado] = useState<{ id: number; nome: string } | null>(null);

    const { aulaID } = useParams();
    const navigate = useNavigate();

    async function loadAula() {
        try {
            const response = await api.get(`/api/aulas/${aulaID}`);
            const adjustedDate = response.data.dia.split("T", 10)[0];
            setId(response.data.id);
            setTrimestre(response.data.trimestre);
            setDia(adjustedDate);
            setLicao(response.data.licao);
            const professorAula = response.data.professorAulas[0];
                if (professorAula) {
            setProfessorSelecionado({ id: professorAula.idProfessor, nome: professorAula.nomeProfessor });
            } else {
                setProfessorSelecionado(null);
            }
            setAlunosMatriculados(response.data.alunosMatriculados);
            setVisitantes(response.data.visitantes);
            setPresentes(response.data.presentes);
            setAusentes(response.data.ausentes);
            setTotalAssistencia(response.data.totalAssistencia);
            setBiblias(response.data.biblias);
            setRevistas(response.data.revistas);
            setOferta(response.data.oferta);
            setAlunos(response.data.alunoAulas.map((aa: { idAluno: any; nomeAluno: any; presente: any; }) => ({
                id: aa.idAluno,
                nome: aa.nomeAluno,
                presente: aa.presente
            })));
            setTurmas(response.data.aulaTurmas.map((at: { idTurma: any; nomeTurma: any; }) => ({
                id: at.idTurma,
                nome: at.nomeTurma,
            })));
            setTurmaSelecionada(response.data.aulaTurmas[0]?.idTurma || '');
            setIsEditMode(true);
            //console.log("DADOS", response.data)
        } catch (error) {
            alert('Error recovering aula. Try again!');
            navigate('/aulas');
        }
    }

    useEffect(() => {
        if (aulaID === '0') return;
        else loadAula();
    }, [aulaID])

    useEffect(() => {
        const igrejaId = sessionStorage.getItem('igrejaId');
        if (igrejaId && !isNaN(Number(igrejaId))) {
        fetchTurmasPorIgreja(Number(igrejaId))
        .then(response => {
          setTurmas(response.data);
        })
        .catch(error => console.log(error));
        } else {
            fetchTurmas().then(response => setTurmas(response.data))
            .catch(error => console.log(error))
        }
    }, []);


    async function loadAlunosByTurma(turmaId: any) {
        try {
            const response = await api.get(`/api/alunos/turma/${turmaId}`);
            const alunosMatriculados = response.data.length;
            setAlunos(response.data.map((aluno: { id: any; nome: any; }) => ({
                id: aluno.id,
                nome: aluno.nome,
                presente: false
            })));
            setAlunosMatriculados(alunosMatriculados.toString());

            const responseProfessores = await api.get(`/api/professores/turma/${turmaId}`);
            setProfessores(responseProfessores.data);
        } catch (error) {
            alert('Error recovering students. Try again!');
        }
    }

    async function saveOrUpdate(e: { preventDefault: () => void; }) {
        e.preventDefault();

        const alunoAulas = alunos.map(aluno => ({
            idAluno: aluno.id,
            presente: aluno.presente
        }));

        const aulaTurmas = turmaSelecionada ? [{
            idTurma: turmaSelecionada, // Apenas a turma selecionada
            nomeTurma: turmas.find(turma => turma.id === Number(turmaSelecionada))?.nome
        }] : [];

        const data = {
            licao,
            dia,
            alunosMatriculados,
            trimestre,
            ausentes,
            presentes,
            visitantes,
            totalAssistencia,
            biblias,
            revistas,
            oferta,
            professorAulas: [
                {
                    idProfessor: professorSelecionado?.id || null,  // O id do professor selecionado
                },
            ],
            alunoAulas,
            aulaTurmas,
            id: aulaID !== '0' ? id : undefined,
        };

        try {
            if (aulaID === '0') {
                await api.post('/api/aulas', data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            } else {
                data.id = id;
                await api.put(`/api/aulas/${aulaID}`, data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            }
            navigate('/aulas');
        } catch (error) {
            alert('Error while recording aula. Try again!');
        }
    }

    useEffect(() => {
        const calculateTotal = () => {
            const totalAssistencia =
                (parseInt(presentes) || 0) +
                (parseInt(visitantes) || 0);
            setTotalAssistencia(totalAssistencia.toString());
        };

        calculateTotal();
    }, [presentes, visitantes]);

    useEffect(() => {
        const calculateTotal1 = () => {
            const total =
                (parseInt(alunosMatriculados) || 0) -
                (parseInt(presentes) || 0);
            setAusentes(total.toString());
        };

        calculateTotal1();
    }, [alunosMatriculados, presentes]);

    const handleDateChange = (e: { target: { value: SetStateAction<string>; }; }) => {
        setDia(e.target.value);
    };

    const handlePresenceChange = (idAluno: number, presente: boolean) => {
        setAlunos(alunos.map(aluno =>
            aluno.id === idAluno ? { ...aluno, presente } : aluno
        ));
    };

    const handleSelectChange = (event: { target: { value: any; }; }) => {
        const turmaId = event.target.value;
        setTurmaSelecionada(turmaId);
        loadAlunosByTurma(turmaId);
    };

    return (
  <>
    <Header />

    <div className="w-full min-h-screen bg-gradient-to-t from-slate-700 flex flex-col items-center px-3 sm:px-5 py-6">

      {/* TÍTULO */}
      <h1 className="text-3xl sm:text-4xl md:text-5xl text-center mb-8 font-bold gap-4">
        {aulaID === '0' ? 'Add ' : 'Update '}Aula
      </h1>

      {/* TURMAS E ALUNOS */}
      <div className="w-full max-w-7xl flex flex-col lg:flex-row justify-center gap-6 mb-8">

        {/* TURMAS */}
        <div className="w-full lg:w-1/3 bg-white rounded-xl shadow-lg p-4">

          <h2 className="text-gray-900 text-center mb-4 font-bold text-xl">
            Turmas
          </h2>

          {isEditMode ? (

            <p className="text-center break-words">
              {turmas.find(t => t.id === Number(turmaSelecionada))?.nome}
            </p>

          ) : (

            <select
              value={turmaSelecionada}
              onChange={handleSelectChange}
              className="border-2 border-gray-300 p-2 rounded-lg w-full"
            >
              <option value="">
                Selecione uma Turma
              </option>

              {turmas.map((turma) => (
                <option
                  key={turma.id}
                  value={turma.id}
                >
                  {turma.nome}
                </option>
              ))}
            </select>

          )}

        </div>

        {/* ALUNOS */}
        <div className="w-full lg:w-1/2 bg-white rounded-xl shadow-lg p-4 max-h-[500px] overflow-y-auto">

          <h2 className="text-gray-900 text-center mb-4 font-bold text-xl">
            Alunos
          </h2>

          {alunos.map(aluno => (
            <div
              key={aluno.id}
              className="flex items-center justify-between gap-3 mb-3 border-b pb-2"
            >

              <label className="break-words text-sm sm:text-base">
                {aluno.nome}
              </label>

              {isEditMode ? (

                <input
                  type="checkbox"
                  checked={aluno.presente || false}
                  readOnly
                />

              ) : (

                <input
                  type="checkbox"
                  checked={aluno.presente || false}
                  onChange={e =>
                    handlePresenceChange(
                      aluno.id,
                      e.target.checked
                    )
                  }
                />

              )}

            </div>
          ))}

        </div>

      </div>

      {/* FORMULÁRIO */}
      <form
        key={aulaID}
        onSubmit={saveOrUpdate}
        className="bg-gradient-to-t from-zinc-300 w-full max-w-4xl p-4 sm:p-6 rounded-xl shadow-lg mb-6"
      >

        <div className="grid grid-cols-1 md:grid-cols-2 gap-5">

          {/* TRIMESTRE */}
          <div className="flex flex-col gap-2">

            <label
              htmlFor="Trimestre"
              className="font-bold text-lg"
            >
              Trimestre
            </label>

            <select
              id="Trimestre"
              className="border-2 border-gray-300 p-2 rounded-lg w-full"
              value={trimestre}
              onChange={(e) => setTrimestre(e.target.value)}
              required
            >
              <option value="" disabled>
                Selecione o Trimestre
              </option>

              <option value="1º Trimestre">
                1º Trimestre
              </option>

              <option value="2º Trimestre">
                2º Trimestre
              </option>

              <option value="3º Trimestre">
                3º Trimestre
              </option>

              <option value="4º Trimestre">
                4º Trimestre
              </option>

            </select>

          </div>

          {/* DIA */}
          <div className="flex flex-col gap-2">

            <label
              htmlFor="Dia"
              className="font-bold text-lg"
            >
              Dia
            </label>

            <input
              id="Dia"
              className="border-2 border-gray-300 p-2 rounded-lg w-full"
              type="date"
              value={dia}
              onChange={handleDateChange}
              required
            />

          </div>

          {/* LIÇÃO */}
          <div className="flex flex-col gap-2">

            <label
              htmlFor="Licao"
              className="font-bold text-lg"
            >
              Lição
            </label>

            <input
              id="Licao"
              className="border-2 border-gray-300 p-2 rounded-lg w-full"
              type="text"
              placeholder="Lição"
              value={licao}
              onChange={(e) => setLicao(e.target.value)}
              required
            />

          </div>

          {/* PROFESSOR */}
          <div className="flex flex-col gap-2">

            <label
              htmlFor="NomeProfessor"
              className="font-bold text-lg"
            >
              Nome do Professor
            </label>

            {isEditMode ? (

              <p className="break-words">
                {professorSelecionado?.nome ||
                  'Nome do professor não informado'}
              </p>

            ) : (

              <select
                value={professorSelecionado?.id || ''}
                className="border-2 border-gray-300 p-2 rounded-lg w-full"
                onChange={(e) => {
                  const selectedId = Number(e.target.value);

                  const selectedProfessor =
                    professores.find(
                      prof => prof.id === selectedId
                    );

                  setProfessorSelecionado(
                    selectedProfessor || null
                  );
                }}
              >

                <option value="">
                  Selecione um professor
                </option>

                {professores.map((professor) => (
                  <option
                    key={professor.id}
                    value={professor.id}
                  >
                    {professor.nome}
                  </option>
                ))}

              </select>

            )}

          </div>

          {/* CAMPOS NUMÉRICOS */}
          {[
            {
              label: 'Alunos Matriculados',
              value: alunosMatriculados,
              setter: setAlunosMatriculados,
              id: 'AlunosMatriculados'
            },
            {
              label: 'Visitantes',
              value: visitantes,
              setter: setVisitantes,
              id: 'Visitantes'
            },
            {
              label: 'Presentes',
              value: presentes,
              setter: setPresentes,
              id: 'Presentes'
            },
            {
              label: 'Ausentes',
              value: ausentes,
              setter: setAusentes,
              id: 'Ausentes'
            },
            {
              label: 'Total de Assistência',
              value: totalAssistencia,
              setter: setTotalAssistencia,
              id: 'TotalAssistencia'
            },
            {
              label: 'Bíblias',
              value: biblias,
              setter: setBiblias,
              id: 'Biblias'
            },
            {
              label: 'Revistas',
              value: revistas,
              setter: setRevistas,
              id: 'Revistas'
            },
            {
              label: 'Oferta',
              value: oferta,
              setter: setOferta,
              id: 'Oferta',
              step: '0.01'
            }
          ].map((field) => (

            <div
              key={field.id}
              className="flex flex-col gap-2"
            >

              <label
                htmlFor={field.id}
                className="font-bold text-lg"
              >
                {field.label}
              </label>

              <input
                id={field.id}
                className="border-2 border-gray-300 p-2 rounded-lg w-full"
                type="number"
                step={field.step || undefined}
                placeholder={field.label}
                value={field.value}
                onChange={(e) =>
                  field.setter(e.target.value)
                }
                required
              />

            </div>

          ))}

        </div>

        {/* BOTÕES */}
        <div className="flex flex-row sm:flex-row justify-center items-center gap-4 mt-10 mb-5">

          <button
            className="flex items-center justify-center gap-2 bg-green-500 hover:bg-green-700
             text-white font-bold py-2 px-5 rounded transition"
            type="submit"
          >
            <span>Adicionar</span>
            <BiSend size={24} />
          </button>


          <Button
            variant="primary"
            className="btn-primary"
            as="a"
            href="/aulas"
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

export default NewAula;

