/* eslint-disable @typescript-eslint/no-unused-vars */
import { SetStateAction, useEffect, useState } from "react";
import Footer from "../../components/footer/footer";
import Header from "../../components/header/header";
import { Igreja } from "../../types/igreja";
import { fecthVincularPerfil, fetchAlunoPorIgreja, fetchIgrejas, fetchPerfilPorNome, fetchProfessorPorIgreja } from "../../utils/api";
import { Professor } from "../../types/professor";
import { Aluno } from "../../types/aluno";
import { Button } from "react-bootstrap";
import { Perfil } from "../../types/perfil";







const areas = [
    { value: "AREA_01", label: "Área 1" },
    { value: "AREA_02", label: "Área 2" },
    { value: "AREA_03", label: "Área 3" },
    { value: "AREA_04", label: "Área 4" },
    { value: "AREA_05", label: "Área 5" },
    { value: "AREA_06", label: "Área 6" },
    { value: "AREA_07", label: "Área 7" },
    { value: "AREA_08", label: "Área 8" },
    { value: "AREA_09", label: "Área 9" },
    { value: "AREA_10", label: "Área 10" },
    { value: "AREA_11", label: "Área 11" }
];


function VincularPerfil() {
  const [igreja, setIgreja] = useState<Igreja[]>([]);
  const [professor, setProfessor] = useState<Professor[]>([]);
  const [aluno, setAluno] = useState<Aluno[]>([]);
  const [area, setArea] = useState('');
  const [entidade, setEntidade] = useState('');
  const [filteredIgrejas, setFilteredIgrejas] = useState<Igreja[]>([]);
  const [showPopup, setShowPopup] = useState(false);
  const [igrejaId, setIgrejaId] = useState('');
  const [selectedAluno, setSelectedAluno] = useState<Aluno | null>(null);
  const [selectedProfessor, setSelectedProfessor] = useState<Professor | null>(null);
  const [perfil, setPerfil] = useState<Perfil[]>([]);
  const [selectedPerfil, setSelectedPerfil] = useState<Perfil | null>(null);

  useEffect(() => {
    fetchIgrejas()
      .then((response) => setIgreja(response.data))
      .catch((error) => console.log(error));
  }, []);

  useEffect(() => {
    if (area) {
      const filtered = igreja.filter((ig) => ig.area === area);
      setFilteredIgrejas(filtered);
      if (filtered.length === 0) {
        setShowPopup(true);
      } else {
        setShowPopup(false);
      }
    } else {
      setFilteredIgrejas([]);
      setShowPopup(false);
    }
  }, [area, igreja]);

  const handleSearchPerfil = (nome: string) => {
    if (nome.trim() === '') {
      setPerfil([]);
      return;
    }
    fetchPerfilPorNome(nome.toUpperCase())
      .then((response) => setPerfil(response.data))
      .catch((error) => console.log(error));
  };

  useEffect(() => {
    if (igrejaId && !isNaN(Number(igrejaId))) {
      if (entidade === 'aluno') {
        fetchAlunoPorIgreja(Number(igrejaId))
          .then((response) => setAluno(response.data))
          .catch((error) => console.log(error));
      } else if (entidade === 'professor') {
        fetchProfessorPorIgreja(Number(igrejaId))
          .then((response) => setProfessor(response.data))
          .catch((error) => console.log(error));
      }
    }
  }, [igrejaId, entidade]);

  const handleIgrejaSelect = (id: string) => {
    setIgrejaId(id);
    setAluno([]);
    setProfessor([]);
    setSelectedAluno(null);
    setSelectedProfessor(null);
  };

  const handleAlunoSelect = (aluno: Aluno) => {
    setSelectedAluno(aluno);
    setSelectedProfessor(null);
  };

  const handleProfessorSelect = (prof: Professor) => {
    setSelectedProfessor(prof);
    setSelectedAluno(null);
  };

  const handleUsuarioSelect = (perfil: Perfil) => {
    setSelectedPerfil(perfil);
  };

  const handleVincular = () => {
    if (!selectedPerfil) {
      alert('Por favor, selecione um usuário.');
      return;
    }

    if (entidade === 'aluno' && !selectedAluno) {
      alert('Por favor, selecione um aluno.');
      return;
    }

    if (entidade === 'professor' && !selectedProfessor) {
      alert('Por favor, selecione um professor.');
      return;
    }

    const data = {
      tipoEntidade: entidade,
      entidadeId:
        entidade === 'aluno' ? selectedAluno?.id ?? 0 : selectedProfessor?.id ?? 0,
      perfilId: selectedPerfil?.id ?? 0,
    };

    fecthVincularPerfil(data)
      .then(() => alert('Perfil vinculado com sucesso!'))
      .catch((error) => console.log(error));
  };

  return (
    <>
      <Header />
      <div className="flex justify-center px-4">
        <div className="flex flex-col text-center bg-slate-100 w-full sm:w-3/4 lg:w-3/6 h-full mb-5 rounded-5 p-5">
          
          {/* Select Área */}
          <select
            onChange={(e) => setArea(e.target.value)}
            value={area}
            className="mb-6 bg-sky-400 h-8 rounded-2 w-full sm:w-auto"
          >
            <option value="">Selecione a Área</option>
            {areas.map((area) => (
              <option key={area.value} value={area.value}>
                {area.label}
              </option>
            ))}
          </select>

          {showPopup && (
            <div className="popup mb-5">
              <p>
                Não há igrejas na área selecionada. Por favor, entre em contato com o
                administrador do sistema.
              </p>
              <Button onClick={() => setShowPopup(false)}>Fechar</Button>
            </div>
          )}

          {/* Select Igreja */}
          <select
            onChange={(e) => handleIgrejaSelect(e.target.value)}
            value={igrejaId}
            className="mb-6 bg-sky-400 h-8 rounded-2 w-full sm:w-auto"
          >
            <option value="">Selecione a Igreja</option>
            {filteredIgrejas.map((ig) => (
              <option key={ig.id} value={ig.id}>
                {ig.nome}
              </option>
            ))}
          </select>

          <p>Buscar Por :</p>
          <div className="flex flex-col sm:flex-row gap-6 justify-center mb-5">
            <label>
              <input
                type="radio"
                value="aluno"
                checked={entidade === 'aluno'}
                onChange={(e) => setEntidade(e.target.value)}
                className="mr-2"
              />
              Aluno
            </label>

            <label>
              <input
                type="radio"
                value="professor"
                checked={entidade === 'professor'}
                onChange={(e) => setEntidade(e.target.value)}
                className="mr-2"
              />
              Professor
            </label>
          </div>

          {/* Grid Alunos */}
          {entidade === 'aluno' && aluno.length > 0 && (
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-2 mb-5 w-full">
              {aluno.map((al) => {
                const partes = al.nome.split(' ');
                const primeiroNome = partes[0];
                const ultimoNome =
                  partes.length > 1 ? partes[partes.length - 1] : '';
                const nomeExibido = `${primeiroNome} ${ultimoNome}`;

                return (
                  <div
                    key={al.id}
                    onClick={() => handleAlunoSelect(al)}
                    className={`p-2 border rounded cursor-pointer text-center ${
                      selectedAluno?.id === al.id
                        ? 'bg-blue-200 border-blue-400'
                        : 'bg-white hover:bg-gray-100'
                    }`}
                  >
                    {nomeExibido}
                  </div>
                );
              })}
            </div>
          )}

          {/* Grid Professores */}
          {entidade === 'professor' && professor.length > 0 && (
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-2 mb-5 w-full">
              {professor.map((prof) => {
                const partes = prof.nome.split(' ');
                const primeiroNome = partes[0];
                const ultimoNome =
                  partes.length > 1 ? partes[partes.length - 1] : '';
                const nomeExibido = `${primeiroNome} ${ultimoNome}`;

                return (
                  <div
                    key={prof.id}
                    onClick={() => handleProfessorSelect(prof)}
                    className={`p-2 border rounded cursor-pointer text-center ${
                      selectedProfessor?.id === prof.id
                        ? 'bg-blue-200 border-blue-400'
                        : 'bg-white hover:bg-gray-100'
                    }`}
                  >
                    {nomeExibido}
                  </div>
                );
              })}
            </div>
          )}

          <p className="mt-5">Digite o nome do perfil</p>
          <input
            type="text"
            placeholder="Buscar Usuário"
            onChange={(e) => handleSearchPerfil(e.target.value)}
            className="mb-5 bg-sky-400 h-8 rounded-2 p-1 w-full sm:w-auto"
          />

          {/* Grid Perfis */}
          {perfil.length > 0 && (
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-2 mt-2 w-full">
              {perfil.map((p) => (
                <div
                  key={p.id}
                  onClick={() => handleUsuarioSelect(p)}
                  className={`p-2 border rounded cursor-pointer text-center ${
                    selectedPerfil?.id === p.id
                      ? 'bg-blue-200 border-blue-400'
                      : 'bg-white hover:bg-gray-100'
                  }`}
                >
                  {p.nome}
                </div>
              ))}
            </div>
          )}

          {/* Botões */}
          <div className="mt-6 flex flex-row sm:flex-row justify-center gap-6">
            <Button
              onClick={handleVincular}
              variant="info"
              disabled={
                !selectedPerfil ||
                (entidade === 'aluno' && !selectedAluno) ||
                (entidade === 'professor' && !selectedProfessor)
              }
            >
              Vincular
            </Button>

            <Button variant="primary" as="a" href="/listausuarios">
              VOLTAR
            </Button>
          </div>
        </div>
      </div>
      <footer>
        <Footer />
      </footer>
    </>
  );

}

export default VincularPerfil;
