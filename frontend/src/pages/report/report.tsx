/* eslint-disable @typescript-eslint/no-unused-vars */
import { Button, Table } from "react-bootstrap";
import Footer from "../../components/footer/footer";
import Header from "../../components/header/header";
import { useEffect, useState } from "react";
import { fetchEbdPorIgreja, fetchEbds, fetchRelatorio } from "../../utils/api";
import { Relatorio } from "../../types/relatorio";
import { Ebd } from "../../types/ebd";
import generatePDF from "../../components/complementos/relatorioPdf";

function Report() {

  const [relatorio, setRelatorio] = useState<Relatorio | null>(null);
  const [idEbd, setIdEbd] = useState('');
  const [data, setData] = useState('');
  const [ano, setAno] = useState('');
  const [mes, setMes] = useState('');
  const [trimestre, setTrimestre] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [ebds, setEbds] = useState<Ebd[]>([]);


  const turmaCount = relatorio?.turmas?.length ?? 0;

  const getContainerStyle = (rows: number): React.CSSProperties => {
    if (rows >= 5) {
      return { maxHeight: '360px', overflowY: 'auto' };
    }
    if (rows >= 3) {
      return { marginBottom: '4rem' }; // empurra footer para baixo
    }
    return {};
  };


  useEffect(() => {
    const igrejaId = sessionStorage.getItem('igrejaId');
    if (igrejaId && !isNaN(Number(igrejaId))) {
      fetchEbdPorIgreja(Number(igrejaId))
        .then(response => {
          const data = response.data;
          // Verifica se a resposta é um objeto e transforma em um array
          setEbds(Array.isArray(data) ? data : [data]);
        })
        .catch(error => console.log(error));
    } else {
      fetchEbds()
        .then((response) => setEbds(response.data))
        .catch((error) => console.log(error));
    }
  }, []);

  // limpa relatório e erro sempre que qualquer filtro mudar
  useEffect(() => {
    setRelatorio(null);
    setError('');
  }, [idEbd, data, mes, trimestre, ano]);
  


  const handleSubmit = async (e: { preventDefault: () => void; }) => {
    e.preventDefault();
    if (!idEbd || (!data && !mes && !trimestre) ) {
        setError('Por favor, selecione uma EBD e escolha uma data, mês ou trimestre.');
        return;
      }
    setLoading(true);
    setError('');
    try {
      const response = await fetchRelatorio(
        data,
        Number(idEbd),
        mes ? Number(mes) : undefined,
        trimestre ? Number(trimestre) : undefined,
        ano ? Number(ano) : undefined
    );
      setRelatorio(response.data);
        } catch (err) {
      setError('Erro ao gerar o relatório.');
        } finally {
      setLoading(false);
        }
  };

  const handleDataChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      setData(e.target.value);
      if (e.target.value) {
        setMes('');
        setTrimestre('');
      }
  };
  
  const handleMesChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
      setMes(e.target.value);
      if (e.target.value) {
        setData('');
        setTrimestre('');
      }
  };
  
  const handleTrimestreChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
      setTrimestre(e.target.value);
      if (e.target.value) {
        setData('');
        setMes('');
      }
  };

  const handlePDFGeneration = () => {
    const ebdSelecionada = ebds.find(ebd => ebd.id === Number(idEbd));
    const ebdNome = ebdSelecionada ? ebdSelecionada.nome : 'EBD não encontrada';
    const reportDate = data || `${mes ? `Mês: ${mes}/${ano}` : ''} ${trimestre ? `Trimestre: ${trimestre} - Ano: ${ano}` : ''}`;
    
    generatePDF(relatorio, ebdNome, reportDate); // Gera o PDF
  };


  return(
    <>
      <Header/>

      <div className="container w-screen h-screen">
                <h2 className="text-cyan-400 text-center mt-5">Relatório de Secretaria</h2>
                <form onSubmit={handleSubmit} className="mb-4">

                    <div className="form-group mb-2">
                    <label>EBD:</label>
                    <select
                        className="form-control"
                        value={idEbd}
                        onChange={(e) => setIdEbd(e.target.value)}
                      >
                        <option value="">Selecione uma EBD</option>
                        {ebds.map((ebd) => (
                          <option key={ebd.id} value={ebd.id}>
                            {ebd.nome}
                          </option>
                        ))}
                    </select>
                    </div>

                    <div className="form-group mb-2">
                    <label>Data:</label>
                    <input
                        type="date"
                        className="form-control"
                        value={data}
                        onChange={handleDataChange}
                        disabled={mes !== '' || trimestre !== ''}
                    />
                    </div>

                    <div className="form-group mb-2">
                        <label>Ano:</label>
                        <input
                            type="number"
                            className="form-control"
                            value={ano}
                            onChange={(e) => setAno(e.target.value)}
                            placeholder="Opcional: Deixe em branco para usar o ano atual"
                        />
                    </div>


                    <div className="form-group mb-2">
                      <label>Mês:</label>
                      <select
                        className="form-control"
                        value={mes}
                        onChange={handleMesChange}
                        disabled={data !== '' || trimestre !== ''}
                      >
                        <option value="">Selecione o mês</option>
                        {Array.from({ length: 12 }, (_, i) => (
                          <option key={i + 1} value={i + 1}>{new Date(0, i).toLocaleString('pt-BR', { month: 'long' })}</option>
                        ))}
                      </select>
                    </div>

                    <div className="form-group mb-2">
                      <label>Trimestre:</label>
                      <select
                        className="form-control"
                        value={trimestre}
                        onChange={handleTrimestreChange}
                        disabled={data !== '' || mes !== ''}
                      >
                        <option value="">Selecione o trimestre</option>
                        <option value="1">1º Trimestre</option>
                        <option value="2">2º Trimestre</option>
                        <option value="3">3º Trimestre</option>
                        <option value="4">4º Trimestre</option>
                      </select>
                    </div>

                    <button type="submit" className="btn btn-primary mt-3" disabled={loading}>
                    {loading ? 'Carregando...' : 'Gerar Relatório'}
                    </button>

                <div className=" flex gap-4 justify-start h-10 mb-5">

                  <Button  
                    className="btn btn-primary mt-3 w-32 h-10 font-bold"
                    onClick={handlePDFGeneration}
                    disabled={!relatorio}
                    >
                      Gerar PDF

                  </Button>

                  
                  <div className="mt-3">
                    <Button as="a" href="/home" className="w-32">
                      VOLTAR
                    </Button>         
                  </div>

                </div>
                      </form>

                {error && <div className="alert alert-danger">{error}</div>}
                
                {relatorio && (
                          <>
                            {/* Tabela de totais gerais */}
                            <h3 className="mt-5 text-sky-400 text-center">Relatório Geral da EBD</h3>
                            <Table striped bordered hover variant="dark">
                              <thead>
                                <tr>
                                  <th>Total Alunos Matriculados</th>
                                  <th>Total Alunos Presentes</th>
                                  <th>Total Alunos Ausentes</th>
                                  <th>Total Visitantes</th>
                                  <th>Total Assistência</th>
                                  <th>Total Bíblias</th>
                                  <th>Total Revistas</th>
                                  <th>Total Oferta</th>
                                </tr>
                              </thead>
                              <tbody>
                                <tr>
                                  <td>{relatorio.totalMatriculados}</td>
                                  <td>{relatorio.totalPresentes}</td>
                                  <td>{relatorio.totalAusentes}</td>
                                  <td>{relatorio.totalVisitantes}</td>
                                  <td>{relatorio.totalAssistencia}</td>
                                  <td>{relatorio.totalBiblias}</td>
                                  <td>{relatorio.totalRevistas}</td>
                                  <td>{relatorio.totalOferta?.toFixed(2)}</td>
                                </tr>
                              </tbody>
                            </Table>

                            {/* Tabela de relatórios das turmas */}
                            {relatorio.turmas && relatorio.turmas.length > 0 && (
                              <>
                                <h3 className="mt-5 text-sky-400 text-center">Detalhes por Turma</h3>
                                <Table striped bordered hover variant="light">
                                  <thead>
                                    <tr>
                                      <th>Nome da Turma</th>
                                      <th>Total Matriculados</th>
                                      <th>Total Presentes</th>
                                      <th>Total Ausentes</th>
                                      <th>Total Visitantes</th>
                                      <th>Total Assistência</th>
                                      <th>Total Bíblias</th>
                                      <th>Total Revistas</th>
                                      <th>Total Oferta</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                    {relatorio.turmas.map((turma, index) => (
                                      <tr key={index}>
                                        <td>{turma.nomeTurma}</td>
                                        <td>{turma.totalMatriculados}</td>
                                        <td>{turma.totalPresentes}</td>
                                        <td>{turma.totalAusentes}</td>
                                        <td>{turma.totalVisitantes}</td>
                                        <td>{turma.totalAssistencia}</td>
                                        <td>{turma.totalBiblias}</td>
                                        <td>{turma.totalRevistas}</td>
                                        <td>{turma.totalOferta?.toFixed(2)}</td>
                                      </tr>
                                    ))}
                                  </tbody>
                                </Table>
                              </>
                            )}
                          </>
                        )}

      </div>

      <footer className="mt-64">
        <Footer/>
      </footer>
        
    </>
  )
}

export default Report;