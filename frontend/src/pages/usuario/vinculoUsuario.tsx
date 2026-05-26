/* eslint-disable @typescript-eslint/no-unused-vars */
import { SetStateAction, useEffect, useState } from "react";
import Footer from "../../components/footer/footer";
import Header from "../../components/header/header";
import { Igreja } from "../../types/igreja";
import { fecthVincularUsuario, fetchAlunoPorIgreja, fetchIgrejas, 
    fetchProfessorPorIgreja, fetchUsuarioPorNome, fetchUserPorIgreja } from "../../utils/api";
import { Usuario } from "../../types/usuario";
import { Professor } from "../../types/professor";
import { Aluno } from "../../types/aluno";
import { Button } from "react-bootstrap";







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


function VincularUsuario() {

    const [ igrejas, setIgrejas] =  useState<Igreja[]>([]);
    const [ professor, setProfessor] =  useState<Professor[]>([]);
    const [ aluno, setAluno] =  useState<Aluno[]>([]);
    const [ area, setArea] = useState('');
    const [ entidade, setEntidade] = useState('');
    const [ selectedIgreja, setSelectedIgreja] = useState('');
    const [ filteredIgrejas, setFilteredIgrejas] = useState<Igreja[]>([]);
    const [ showPopup, setShowPopup] = useState(false);
    const [ igrejaId, setIgrejaId] = useState('');
    const [ selectedAluno, setSelectedAluno] = useState<Aluno | null>(null);
    const [ selectedProfessor, setSelectedProfessor] = useState<Professor | null>(null);
    const [ usuario, setUsuario] = useState<Usuario[]>([]);
    const [ selectedUsuario, setSelectedUsuario] = useState<Usuario | null>(null);
    const [ userProfile, setUserProfile] = useState('');


    useEffect(() => {
    const storedUserProfile = sessionStorage.getItem('userProfile');
    if (storedUserProfile) {
      setUserProfile(storedUserProfile);
    }
    fetchIgrejas()
        .then(response => setIgrejas(response.data))
        .catch(error => console.log(error));
    }, []);

   useEffect(() => {
    if (area) {
        const igrejasFiltradas = igrejas.filter(ig => ig.area === area);
        setFilteredIgrejas(igrejasFiltradas);
        setIgrejaId('');
        setSelectedIgreja('');
        setAluno([]);
        setProfessor([]);
        setSelectedAluno(null);
        setSelectedProfessor(null);
        if (igrejasFiltradas.length === 0) {
        setShowPopup(true);
        } else {
        setShowPopup(false);
        }
    } else {
        setFilteredIgrejas([]);
        setIgrejaId('');
        setSelectedIgreja('');
    }
    }, [area, igrejas]);



    const handleSearchUsuario = (nome: string) => {
        const term = nome.trim();
        if (term.trim() === "") {
            setUsuario([]);
            return;
        }
        const upper = term.toUpperCase();
        if (igrejaId && !isNaN(Number(igrejaId))) {
            fetchUserPorIgreja(Number(igrejaId))
                .then(response => {
                    const lista = Array.isArray(response.data) ? response.data : [];
                    const filtered = lista.filter((u: Usuario) =>
                        String(u.nome).toUpperCase().includes(upper)
                    );
                    setUsuario(filtered);
                })
                .catch(error => {
                    console.error('Erro ao buscar usuários por igreja:', error);
                    setUsuario([]);
                });
            return;
        }

       
    };

    useEffect(() => {
        if (igrejaId && !isNaN(Number(igrejaId))) {
          if (entidade === 'aluno') {
            fetchAlunoPorIgreja(Number(igrejaId))
              .then(response => setAluno(response.data))
              .catch(error => console.log(error));
          } else if (entidade === 'professor') {
            fetchProfessorPorIgreja(Number(igrejaId))
              .then(response => setProfessor(response.data))
              .catch(error => console.log(error));
          }
        }
      }, [igrejaId, entidade]);

    const handleIgrejaSelect = (id: SetStateAction<string>) => {
        setIgrejaId(id);
        setSelectedIgreja(id);
        setAluno([]);
        setProfessor([]);
        setSelectedAluno(null);
        setSelectedProfessor(null);
    };

    const handleEntidadeChange = (value: SetStateAction<string>) => {
        setEntidade(value);
    };

    const handleAlunoSelect = (aluno: Aluno) => {
        setSelectedAluno(aluno);
        console.log("Aluno selecionado:", aluno);
    };
    
    const handleProfessorSelect = (prof: Professor) => {
        setSelectedProfessor(prof);
        console.log("Professor selecionado:", prof);
    };
    
    const handleUsuarioSelect = (user: Usuario) => {
        setSelectedUsuario(user);
        console.log("Usuário selecionado:", user);
    };
    


    const handleVincular = () => {
        if (!selectedUsuario) {
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
          entidadeId: entidade === 'aluno' ? selectedAluno?.id ?? 0 : selectedProfessor?.id ?? 0,
          usuarioId: selectedUsuario?.id ?? 0
        };
      
        fecthVincularUsuario(data)
          .then(response => alert('Usuario vinculado com sucesso!'))
          .catch(error => console.log(error));
    };
      
    const isVincularDisabled = !selectedUsuario || 
    (entidade === 'aluno' && !selectedAluno) ||
    (entidade === 'professor' && !selectedProfessor);
    
    const getPrimeiroEUltimoNome = (nomeCompleto: string) => {
        if (!nomeCompleto) return "";
        const partes = nomeCompleto.trim().split(" ");
        if (partes.length === 1) return partes[0]; // só tem um nome
        return `${partes[0]} ${partes[partes.length - 1]}`; // primeiro + último
    };


    return(
  <>
    <Header/>
    <h2 className="text-center text-2xl font-bold">Vincular Usuário</h2>
    <div className="flex justify-center px-4">
      <div className="flex flex-col text-center bg-slate-100 w-full sm:w-3/4 h-full mb-5 rounded-5 p-4">
        
        {/* Select Área */}
        <select 
          onChange={(e) => setArea(e.target.value)} 
          value={area} 
          className="mb-6 bg-sky-400 h-8 rounded-2 w-full sm:w-auto"
        >
          <option value="">Selecione a Área</option>
          {areas.map(area => (
            <option key={area.value} value={area.value}>{area.label}</option>
          ))}
        </select>

        {showPopup && (
          <div className="popup mb-5">
            <p>Não há igrejas na área selecionada. Por favor, entre em contato com o administrador do sistema.</p>
            <Button onClick={() => setShowPopup(false)}>Fechar</Button>
          </div>
        )}

        {/* Select Igreja */}
        <select 
          onChange={(e) => handleIgrejaSelect(e.target.value)} 
          value={igrejaId} 
          className="mb-6 bg-sky-400 h-8 rounded-2 w-full sm:w-auto"
          disabled={!area || filteredIgrejas.length === 0}
        >
          <option value="">
            {!area ? "Selecione a Área primeiro" : filteredIgrejas.length === 0 ? "Nenhuma igreja disponível" : "Selecione a Igreja"}
          </option>
          {filteredIgrejas.map(ig => (
            <option key={ig.id} value={ig.id}>{ig.nome}</option>
          ))}
        </select>

        <p>Buscar Por :</p>
        <div className="flex flex-col sm:flex-row gap-4 justify-center mb-6">
          <label>
            <input 
              type="radio" 
              value="aluno" 
              checked={entidade === 'aluno'} 
              onChange={(e) => handleEntidadeChange(e.target.value)} 
              className="mr-2"
            />
            Aluno
          </label>
          <label>
            <input 
              type="radio" 
              value="professor" 
              checked={entidade === 'professor'} 
              onChange={(e) => handleEntidadeChange(e.target.value)} 
              className="mr-2"
            />
            Professor
          </label>
        </div>

        {/* Grid Alunos */}
        {entidade === 'aluno' && (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-2 w-full px-2">
            {aluno.map(al => (
              <div
                key={al.id}
                onClick={() => handleAlunoSelect(al)}
                className={`p-2 border rounded cursor-pointer text-sm ${
                  selectedAluno?.id === al.id ? 'bg-blue-200' : 'bg-white'
                } hover:bg-blue-100`}
              >
                {getPrimeiroEUltimoNome(al.nome)}
              </div>
            ))}
          </div>
        )}

        {/* Grid Professores */}
        {entidade === 'professor' && (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-2 w-full px-2">
            {professor.map(prof => (
              <div
                key={prof.id}
                onClick={() => handleProfessorSelect(prof)}
                className={`p-2 border rounded cursor-pointer text-sm ${
                  selectedProfessor?.id === prof.id ? 'bg-blue-200' : 'bg-white'
                } hover:bg-blue-100`}
              >
                {getPrimeiroEUltimoNome(prof.nome)}
              </div>
            ))}
          </div>
        )}

        <p className="mt-5">Digite o nome do usuário</p>
        <input
          type="text"
          placeholder="Buscar Usuário"
          onChange={(e) => handleSearchUsuario(e.target.value)}
          className="mb-5 bg-sky-400 h-8 rounded-2 p-1 w-full sm:w-auto"
        />

        {/* Grid Usuários */}
        {usuario.length > 0 && (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-2 mt-2 w-full">
            {usuario.map((user) => {
              const partes = user.nome.split(' ');
              const primeiroNome = partes[0];
              const ultimoNome = partes.length > 1 ? partes[partes.length - 1] : '';
              const nomeExibido = `${primeiroNome} ${ultimoNome}`;

              return (
                <div
                  key={user.id}
                  onClick={() => handleUsuarioSelect(user)}
                  className={`p-2 border rounded cursor-pointer text-center ${
                    selectedUsuario?.id === user.id ? 'bg-blue-200 border-blue-400' : 'bg-white hover:bg-gray-100'
                  }`}
                >
                  {nomeExibido}
                </div>
              );
            })}
          </div>
        )}

        {/* Botões */}
        <div className="mt-6 flex flex-row sm:flex-row justify-center gap-4">
          <Button 
            onClick={handleVincular} 
            variant="info"
            disabled={isVincularDisabled}
            className={`${isVincularDisabled ? 'opacity-50 cursor-not-allowed' : ''}`}
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
      <Footer/>
    </footer>
  </>
)

}

export default VincularUsuario;