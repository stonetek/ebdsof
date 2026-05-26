import { useState, useEffect } from "react";
import { fetchAlunosElegiveis, fetchEbdAndTurmasByIgrejas, fetchIgrejas, matricularAlunos} from "../../utils/api";
import { Button } from "react-bootstrap";
import Footer from "../../components/footer/footer";
import Header from "../../components/header/header";


// Interface para Igreja
interface Igreja {
    id: number;
    nome: string;
  }
  
  // Interface para Aluno
  interface Aluno {
    id: number;
    nome: string;
    // Outros campos do aluno
  }
  
  // Interface para Turma dentro de uma EBD
  interface EbdTurmaDTO {
    id: number;
    nomeTurma: string;
    // Outros campos que possam estar presentes no DTO da Turma
  }
  
  // Interface para IgrejaEbdTurmasDTO
  export interface IgrejaEbdTurmasDTO {
    idEbd: number;
    ebdNome: string;
    turmas: EbdTurmaDTO[];
  }
  
  
  
  const Matriculas = () => {
    const [igrejas, setIgrejas] = useState<Igreja[]>([]);
    const [igrejaEbdTurmas, setIgrejaEbdTurmas] = useState<IgrejaEbdTurmasDTO[]>([]);
    const [selectedIgreja, setSelectedIgreja] = useState<number | null>(null);
    const [selectedTurma, setSelectedTurma] = useState<number | null>(null);
    const [alunos, setAlunos] = useState<Aluno[]>([]);
    const [selectedAlunos, setSelectedAlunos] = useState<number[]>([]);
  
    // Carrega todas as igrejas na montagem do componente
    useEffect(() => {
      const loadIgrejas = async () => {
        const response = await fetchIgrejas();
        setIgrejas(response.data);
      };
      loadIgrejas();
    }, []);

  
    // Função para lidar com a mudança na seleção da Igreja
    async function handleIgrejaChange(event: React.ChangeEvent<HTMLSelectElement>) {
      const idIgreja = Number(event.target.value);
      setSelectedIgreja(idIgreja);
      if (isNaN(idIgreja)) {
        setSelectedIgreja(null);
        setIgrejaEbdTurmas([]);
        setSelectedTurma(null);
        setAlunos([]);
        return;
      } 
      setSelectedIgreja(idIgreja);
      setIgrejaEbdTurmas([]);
      setSelectedTurma(null);
      setAlunos([]);  
      try {
        const response = await fetchEbdAndTurmasByIgrejas(idIgreja);
        setIgrejaEbdTurmas(response.data);
      } catch (error) {
        console.error("Erro ao buscar Ebd e Turmas da Igreja:", error);
      }
    }
  
    // Função para lidar com a mudança na seleção da Turma
    async function handleTurmaChange(event: React.ChangeEvent<HTMLSelectElement>) {
      const idTurma = Number(event.target.value);
      setSelectedTurma(idTurma);
      
      if (isNaN(idTurma)) {
        setSelectedTurma(null);
        setAlunos([]);
        return;
      }
  
      setSelectedTurma(idTurma);
      setAlunos([]);
  
      try {
        const response = await fetchAlunosElegiveis(idTurma);
        setAlunos(response.data);
      } catch (error) {
        console.error("Erro ao buscar alunos da Turma:", error);
      }
    }
  
    // Função para selecionar alunos
    const handleAlunoSelect = (alunoId: number) => {
      setSelectedAlunos((prevSelected) =>
        prevSelected.includes(alunoId)
          ? prevSelected.filter((id) => id !== alunoId)
          : [...prevSelected, alunoId]
      );
    };
  
    // Função para matricular alunos
    const handleMatricularClick = async () => {
        if (selectedTurma === null || selectedAlunos.length === 0) {
          console.warn('Nenhuma turma ou aluno selecionado para matricular.');
          return;
        }
    
        try {
          // Chama a função de matrícula importada do serviço
          await matricularAlunos(selectedTurma, null, selectedAlunos);
        } catch (error) {
          console.error('Failed to matriculate alunos', error);
        }
      };
  
    return (
        <>

            <header>
                <Header/>
            </header>

            <div className="h-screen flex-col text-center bg-cyan-100">

                {/* Seleção de Igreja */}
                <select onChange={handleIgrejaChange} value={selectedIgreja || ''} className="mb-4 text-start">
                    <option value="0">Selecione uma Igreja</option>
                    {igrejas.map(igreja => (
                        <option key={igreja.id} value={igreja.id}>{igreja.nome}</option>
                    ))}
                </select>
        
                {/* Exibição das Ebds e Turmas carregadas */}
                {igrejaEbdTurmas.length > 0 && (
                <div className="mb-4">
                    {igrejaEbdTurmas.map(ebdTurma => (
                    <div key={ebdTurma.idEbd}>
                        <h2>{ebdTurma.ebdNome}</h2>
                        <select onChange={handleTurmaChange} value={selectedTurma || ''}>
                        <option value="">Selecione uma Turma</option>
                        {ebdTurma.turmas.map(turma => (
                            <option key={turma.id} value={turma.id}>{turma.nomeTurma}</option>
                        ))}
                        </select>
                    </div>
                    ))}
                </div>
                )}
        
                {/* Listagem de alunos e botão para matricular */}
                {alunos.length > 0 && (
                <div>
                    <h3>Alunos</h3>
                    {alunos.map(aluno => (
                    <div key={aluno.id} className="text-start">
                        <input
                        type="checkbox"
                        checked={selectedAlunos.includes(aluno.id)}
                        onChange={() => handleAlunoSelect(aluno.id)}
                        className="mr-2"
                        />
                        {aluno.nome}
                    </div>
                    ))}
                    <Button onClick={handleMatricularClick} className="mt-5 mb-5">Matricular Alunos</Button>
                </div>
                )}

            <Button as="a" href="/home">VOLTAR</Button>
            </div>
            <footer>
                <Footer />
            </footer>
        </>
    );
  };
  
  export default Matriculas;