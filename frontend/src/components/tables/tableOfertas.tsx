/* eslint-disable @typescript-eslint/no-unused-vars */
import { useState, useEffect, FormEvent, ChangeEvent } from "react";
import { fetchOfertas, fetchTurmas, fetchTurmasPorIgreja } from "../../utils/api";
import { Button, Table } from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import { Bar } from "react-chartjs-2";
import Footer from "../footer/footer";



interface Turma {
    id: number;
    nome: string;
  }
  
  interface OfertaResponse {
    ano: number;
    mes: number | null;
    trimestre: number | null;
    totalOfertas: number;
  }

function DataTableOfertas() {

    const [turma, setTurma] =  useState<Turma[]>([])
    const [selectedTurma, setSelectedTurma] = useState('');
    const [mes, setMes] = useState('');
    const [trimestre, setTrimestre] = useState('');
    const [search, setSearch] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage] = useState(5);
    const [results, setResults] = useState<OfertaResponse[]>([]);
    const [ano, setAno] = useState(new Date().getFullYear().toString());

    useEffect(() => {
        const igrejaId = sessionStorage.getItem('igrejaId'); 
          if (igrejaId && !isNaN(Number(igrejaId))) {
            fetchTurmasPorIgreja(Number(igrejaId))
            .then(response => {
              setTurma(response.data);
            })
            .catch(error => console.log(error));
          } else {
            fetchTurmas().then(response => setTurma(response.data))
            .catch(error => console.log(error))
            }
      }, []);


    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
    
        try {
          const mesValue = mes ? parseInt(mes, 10) : null;
          const trimestreValue = trimestre ? parseInt(trimestre, 10) : null;
          const response = await fetchOfertas(
            parseInt(selectedTurma, 10),
            mesValue,
            trimestreValue,
            parseInt(ano, 10)
          );
          setResults(response.data);
        } catch (error) {
          console.log(error);
        }
    };
      
    const handleMesChange = (e: ChangeEvent<HTMLSelectElement>) => {
        const selectedMes = e.target.value;
        setMes(selectedMes);
        setTrimestre('');
        if (selectedMes) {
            const trimestreCorrespondente = getTrimestreFromMes(parseInt(selectedMes, 10));
            setTrimestre(trimestreCorrespondente.toString());
        }
    };
    
    const handleTrimestreChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setTrimestre(e.target.value);
        setMes('');
    };

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
    };

    const lowerSearch = search.toLowerCase();
    const filteredTurmas = turma.filter((turma) => turma.
        nome.toLowerCase().includes(lowerSearch));

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentResults = results.slice(indexOfFirstItem, indexOfLastItem);

    const totalPages = Math.ceil(results.length / itemsPerPage);

    const getTrimestreFromMes = (mes: number): number => {
        if (mes >= 1 && mes <= 3) {
          return 1; // 1º trimestre
        } else if (mes >= 4 && mes <= 6) {
          return 2; // 2º trimestre
        } else if (mes >= 7 && mes <= 9) {
          return 3; // 3º trimestre
        } else {
          return 4; // 4º trimestre
        }
      };

    const months = [
        { value: '1', label: 'Janeiro' },
        { value: '2', label: 'Fevereiro' },
        { value: '3', label: 'Março' },
        { value: '4', label: 'Abril' },
        { value: '5', label: 'Maio' },
        { value: '6', label: 'Junho' },
        { value: '7', label: 'Julho' },
        { value: '8', label: 'Agosto' },
        { value: '9', label: 'Setembro' },
        { value: '10', label: 'Outubro' },
        { value: '11', label: 'Novembro' },
        { value: '12', label: 'Dezembro' },
    ];

    const [chartData, setChartData] = useState({
        labels: [],
        datasets: [
            {
                label: 'Total Ofertas',
                data: [],
                backgroundColor: 'rgba(75, 192, 192, 0.6)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
            },
        ],
    });

    const chartOptions = {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true,
          },
        },
        plugins: {
          legend: {
            position: 'top',
          },
          title: {
            display: true,
            text: 'Total de Ofertas por Mês',
          },
        },
    };

    useEffect(() => {
        if (results.length > 0) {
            setChartData({
                labels: results.map(result => result.mes ? `Mês ${result.mes}` : `Trimestre ${result.trimestre}`),
                datasets: [
                    {
                        label: 'Total Ofertas',
                        data: results.map(result => result.totalOfertas),
                        backgroundColor: 'rgba(75, 192, 192, 0.6)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1,
                    },
                ],
            });
        } else {
            // Se results estiver vazio, resetar o chartData
            setChartData({
                labels: [],
                datasets: [
                    {
                        label: 'Total Ofertas',
                        data: [],
                        backgroundColor: 'rgba(75, 192, 192, 0.6)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1,
                    },
                ],
            });
        }
    }, [results]);

    const isDataEmpty = results.length === 0 || results.every(result => result.totalOfertas === 0);
          
    return(
    <>
        
            <div className="container mx-auto p-4 w-screen h-scree">
                <h1 className="font-bold text-sky-400">Relatório Financeiro</h1>
                <form onSubmit={handleSubmit} className="mb-4 mt-10">
                    <div className="mb-4">
                        <label className="block text-gray-700">
                            Turma:
                            <select
                            value={selectedTurma}
                            onChange={(e) => setSelectedTurma(e.target.value)}
                            required
                            className="block w-full mt-1 border-gray-300 rounded-md shadow-sm"
                            >
                            <option value="">Selecione uma turma</option>
                            {turma.map(turma => (
                                <option key={turma.id} value={turma.id}>{turma.nome}</option>
                            ))}
                            </select>
                        </label>
                    </div>
                    <div className="mb-4">
                        <label className="block text-gray-700">
                            Mês:
                            <select
                                value={mes}
                                onChange={handleMesChange}
                                required={!trimestre}
                                className="block w-full mt-1 border-gray-300 rounded-md shadow-sm"
                            >
                                <option value="">Selecione um mês</option>
                                {months.map(month => (
                                    <option key={month.value} value={month.value}>{month.label}</option>
                                ))}
                            </select>
                        </label>
                    </div>

                    <div className="mb-4">
                        <label className="block text-gray-700">
                            Trimestre:
                                <select
                                    value={trimestre}
                                    onChange={handleTrimestreChange}
                                    required={!mes}
                                    className="block w-full mt-1 border-gray-300 rounded-md shadow-sm"
                                >
                                    <option value="">Selecione um trimestre</option>
                                    <option value="1º TRIMESTRE">1º TRIMESTRE</option>
                                    <option value="2º TRIMESTRE">2º TRIMESTRE</option>
                                    <option value="3º TRIMESTRE">3º TRIMESTRE</option>
                                    <option value="4º TRIMESTRE">4º TRIMESTRE</option>
                                </select>
                        </label>
                    </div>
                    <div className="mb-4">
                        <label className="block text-gray-700">
                            Ano:
                            <input
                            type="number"
                            value={ano}
                            onChange={(e) => setAno(e.target.value)}
                            required
                            className="block w-full mt-1 border-gray-300 rounded-md shadow-sm"
                            />
                        </label>
                    </div>

                    <div className="flex justify-center items-center space-x-10">
                        <button type="submit" className="px-4 py-2 bg-blue-500 text-white rounded-md shadow-sm hover:bg-blue-700">
                        Buscar Ofertas
                        </button>
                        <Button variant="primary" className='px-4 py-2' as="a" href="/home">VOLTAR</Button>
                    </div>
                
                </form>

                {/* Render the results in a table */}
                 
                    {isDataEmpty ? (
                        <p className="text-center text-gray-500 mt-10">Selecione os itens para exibição.</p>
                    ) : (
                        <>
                            <div className="overflow-x-auto">
                                <Table striped bordered hover variant="pink" className="min-w-full bg-white border">
                                    <thead>
                                        <tr className="table-dark">
                                            <th className="px-4 py-2 border">Ano</th>
                                            <th className="px-4 py-2 border">Mês</th>
                                            <th className="px-4 py-2 border">Trimestre</th>
                                            <th className="px-4 py-2 border">Total Ofertas</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {results.map((result, index) => (
                                            <tr key={index} className="hover:bg-fuchsia-600">
                                                <td className="px-4 py-2 border">{result.ano}</td>
                                                <td className="px-4 py-2 border">{result.mes !== null ? result.mes : 'N/A'}</td>
                                                <td className="px-4 py-2 border">{result.trimestre !== null ? result.trimestre : 'N/A'}</td>
                                                <td className="px-4 py-2 border">R$ {result.totalOfertas}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </Table>

                                <div className="flex justify-between items-center mt-4">
                                    <Button
                                        onClick={() => handlePageChange(currentPage - 1)}
                                        disabled={currentPage === 1}
                                        className="px-4 py-2 bg-gray-300 text-gray-700 rounded-md shadow-sm hover:bg-gray-400 disabled:opacity-50"
                                    >
                                        Anterior
                                    </Button>
                                    <span>Página {currentPage} de {totalPages}</span>
                                    <Button
                                        onClick={() => handlePageChange(currentPage + 1)}
                                        disabled={currentPage === totalPages}
                                        className="px-4 py-2 bg-gray-300 text-gray-700 rounded-md shadow-sm hover:bg-gray-400 disabled:opacity-50"
                                    >
                                        Próxima
                                    </Button>
                                </div>
                            </div>

                            <div className="container mx-auto w-full h-screen mt-10">
                                <div className="">
                                    <h3 className="text-center text-sky-400">Gráfico de Ofertas</h3>
                                    <Bar data={chartData} options={chartOptions} />
                                </div>
                            </div>
                        </>
                    )}
                    
            </div>
            <footer className="mt-10 bg-white">
                    
                <Footer/>
                    
            </footer>
        </>            
    );

}

export default DataTableOfertas;