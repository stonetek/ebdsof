/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react";
import api from "../../../service/api";
import { useNavigate, useParams } from "react-router-dom";
import { Revista } from "../../../types/revista";
import { Igreja } from "../../../types/igreja";
import { fetchIgrejas, fetchRevistas } from "../../../utils/api";
import Footer from "../../../components/footer/footer";
import Header from "../../../components/header/header";
import { Button } from "react-bootstrap";




function NewPedidos() {
    const areas = [{ value: "AREA_01", label: "Área 1" },
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


    type RevistaAdicionada = {
        id: number;
        nome: string;
        preco: number;
        quantidade: number;
        total: number;
        tipo: string;
        formato: string;
    };
    
    const [ id, setId] = useState(null);
    const [nome, setNome] = useState('');
    const [dataPedido, setDataPedido] = useState('');
    const [dataEntregaPrevista, setDataEntregaPrevista] = useState('');
    const [igrejaId, setIgrejaId] = useState('');
    const [descricao, setDescricao] = useState('');
    const [quantidade, setQuantidade] = useState<number>(1);
    const [total, setTotal] = useState(0);
    const [trimestre, setTrimestre] = useState('');
    const [quantidadeTotal, setQuantidadeTotal] = useState(0);
    const [area, setArea] = useState('');
    const [igrejas, setIgrejas] = useState<Igreja[]>([]);
    const [filteredIgrejas, setFilteredIgrejas] = useState<Igreja[]>([]);
    const [revistas, setRevistas] = useState<Revista[]>([]);
    const [revistasAdicionadas, setRevistasAdicionadas] = useState<RevistaAdicionada[]>([]);
    const [status, setStatus] = useState('');
    const [revistaAtual, setRevistaAtual] = useState<Revista | null>(null);
    const [showPopup, setShowPopup] = useState(false);
    const [showModal, setShowModal] = useState(false);

    
    const {pedidoID} = useParams();
    const history = useNavigate();

    async function loadPedido() {
        try {
            const response = await api.get(`/api/pedidos/${pedidoID}`)
            console.log("Dados carregados do pedido:", response.data);
            setId(response.data.id);
            setNome(response.data.nome);
            setDataPedido(response.data.dataPedido);
            setDataEntregaPrevista(response.data.dataEntregaPrevista);
            setDescricao(response.data.descricao);
            setQuantidade(response.data.quantidade);
            setTotal(response.data.total);
            setStatus(response.data.status);
            setArea(response.data.igrejaAreaNome);
            setIgrejaId(String(response.data.igrejaId));
            setTrimestre(response.data.trimestre);
            const revistasFormatadas = response.data.revistas.map((revista: any) => ({
                id: revista.id,
                nome: revista.revistaNome,
                preco: revista.preco,
                quantidade: revista.quantidade,
                total: revista.quantidade * revista.preco,
                tipo: revista.tipo,
                formato: revista.formato,
            }));
            setRevistasAdicionadas(revistasFormatadas);
        } catch (error) {
            alert('Error recovering pedido. Try again!');
            history('/pedidos')
        }
    }
    
    useEffect(() => {
        if (pedidoID === '0') return;
        else loadPedido();
    }, [pedidoID])
    
    
    useEffect(() => {
    fetchIgrejas()
        .then(response => {
            setIgrejas(response.data);

            // Verifica se existe igrejaId na sessionStorage
            const igrejaIdSession = sessionStorage.getItem('igrejaId');
            if (igrejaIdSession) {
                const igrejaUsuario = response.data.find((i: Igreja) => String(i.id) === String(igrejaIdSession));
                if (igrejaUsuario) {
                    setArea(igrejaUsuario.area);
                    setIgrejaId(String(igrejaUsuario.id));
                }
            }
        })
        .catch(error => console.log(error));
    }, []);


    useEffect(() => {
    if (area && Array.isArray(igrejas) && igrejas.length > 0) {
        let filtered = igrejas.filter(igreja => igreja.area === area);

        // Se estiver editando e a igreja do pedido não estiver no filtro, inclua ela
        if (igrejaId && !filtered.some(i => String(i.id) === String(igrejaId))) {
            const igrejaDoPedido = igrejas.find(i => String(i.id) === String(igrejaId));
            if (igrejaDoPedido) {
                filtered = [igrejaDoPedido, ...filtered];
            }
        }

        setFilteredIgrejas(filtered);
        setShowPopup(filtered.length === 0);
    } else {
        setFilteredIgrejas([]);
        setShowPopup(false);
    }
    }, [area, igrejas, igrejaId]);
    
    
    useEffect(() => {
        fetchRevistas()
            .then(response => setRevistas(response.data))
            .catch(error => console.log(error));
    }, []);

    const handleAddRevista = () => {
        if (!revistaAtual) {
            alert("Selecione uma revista");
            return;
        }
    
        const novaRevista: RevistaAdicionada = {
            id: revistaAtual.id,
            nome: revistaAtual.nome,
            preco: revistaAtual.preco,
            quantidade,
            total: parseFloat((revistaAtual.preco * quantidade).toFixed(2)),
            tipo: revistaAtual.tipo,
            formato: revistaAtual.formato,
        };
    
        setRevistasAdicionadas([...revistasAdicionadas, novaRevista]);
    
        setQuantidadeTotal(quantidadeTotal + quantidade);
        setTotal(total + novaRevista.total);
    
        setQuantidade(1);
        setRevistaAtual(null);
    };
    

    const handleRemoveRevista = (index: number) => {
        const revistaRemovida = revistasAdicionadas[index];
        const novaLista = revistasAdicionadas.filter((_, i) => i !== index);
    
        const novaQuantidadeTotal = quantidadeTotal - revistaRemovida.quantidade;
        const novoTotal = total - revistaRemovida.total;
    
        setRevistasAdicionadas(novaLista);
        setQuantidadeTotal(novaQuantidadeTotal);
        setTotal(novoTotal);
    };
    

    async function saveOrUpdate(e:{ preventDefault: () => void; }) {
        e.preventDefault();

        const data = {
            nome,
            dataPedido,
            dataEntregaPrevista,
            igrejaId,
            area,
            descricao,
            total,
            trimestre,
            status,
            id: pedidoID !== '0' ? id : undefined,
            revistas: revistasAdicionadas.map(({ id, quantidade, nome, tipo, formato }) => ({
                id, 
                quantidade,
                revistaNome: nome,
                tipo,
                formato,
            })),
        };console.log("Dados do pedido:", data);
        //console.log("JSON enviado para a API:", JSON.stringify(data, null, 2));
        try {
            if (pedidoID === '0') {
                await api.post('/api/pedidos', data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            } else {
                data.id = id;
                await api.put(`/api/pedidos/pedido-revista/${pedidoID}`, data, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
            }

            history('/pedidos')
        } catch (error) {
            alert('Error while recording revista Try again!')
        }       
    }

    
    const handleSelectRevista = () => {
        if (!revistaAtual) {
            alert("Selecione uma revista");
            return;
        }
        setShowPopup(true);
    };
    
    const igrejaIdSession = sessionStorage.getItem('igrejaId');
    return (
        <>
        <Header />
        <div className="h-screen flex flex-col items-center mb-40">
            <h1 className="text-4xl mb-4 text-cyan-500 mt-3">
                {pedidoID === "0" ? "Criar Pedido" : "Editar Pedido"}
            </h1>
            <form onSubmit={saveOrUpdate} className="w-3/4 bg-white p-6 rounded shadow-md">
                <div className="mb-4">
                    <label className="block text-gray-700">Área</label>
                    <select
                        value={area}
                        onChange={(e) => setArea(e.target.value)}
                        className="w-full border rounded px-2 py-1"
                        disabled={!!igrejaIdSession}
                    >
                        <option value="">Selecione uma Área</option>
                        {areas.map((a) => (
                            <option key={a.value} value={a.value}>
                                {a.label}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="mb-4">
                    <label className="block text-gray-700">Igreja</label>
                    <select
                        value={igrejaId}
                        onChange={(e) => setIgrejaId(e.target.value)}
                        className="w-full border rounded px-2 py-1"
                        disabled={!!igrejaIdSession}
                    >
                        <option value="">Selecione uma Igreja</option>
                        {filteredIgrejas.map((i) => (
                            <option key={i.id} value={i.id}>
                                {i.nome}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="mb-4">
                    <label className="block text-gray-700">Nome do Pedido</label>
                    <input
                        type="text"
                        value={nome}
                        onChange={(e) => setNome(e.target.value)}
                        className="w-full border rounded px-2 py-1"
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700">Anotações</label>
                    <input
                        type="text"
                        value={descricao}
                        onChange={(e) => setDescricao(e.target.value)}
                        className="w-full border rounded px-2 py-1"
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700">Data de Compra</label>
                    <input
                        type="date"
                        value={dataPedido}
                        onChange={(e) => setDataPedido(e.target.value)}
                        className="w-full border rounded px-2 py-1"
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700">Data de Entrega</label>
                    <input
                        type="date"
                        value={dataEntregaPrevista}
                        onChange={(e) => setDataEntregaPrevista(e.target.value)}
                        className="w-full border rounded px-2 py-1"
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700">Trimestre</label>
                    <select
                        value={trimestre}
                        onChange={(e) => setTrimestre(e.target.value)}
                        className="w-full border rounded px-2 py-1"
                    >
                        <option value="">Selecione um trimestre</option>
                        <option value="1º Trimestre">1º Trimestre</option>
                        <option value="2º Trimestre">2º Trimestre</option>
                        <option value="3º Trimestre">3º Trimestre</option>
                        <option value="4º Trimestre">4º Trimestre</option>
                        
                    </select>
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700">Revistas Disponíveis</label>
                    <select
                        value={revistaAtual?.id || ''}
                        onChange={(e) => {
                            const selectedId = Number(e.target.value);
                            const selectedRevista = revistas.find((r) => r.id === selectedId);
                            setRevistaAtual(selectedRevista || null);
                        }}
                        className="w-full border rounded px-2 py-1"
                    >
                        <option value="">Selecione uma Revista</option>
                        {revistas.map((revista) => (
                            <option key={revista.id} value={revista.id}>
                                {revista.nome} - {revista.tipo} ({revista.formato}) - R$ {revista.preco.toFixed(2)}
                            </option>
                        ))}
                    </select>
                </div>

                {showPopup && (
                    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
                        <div className="bg-white p-6 rounded shadow-md w-1/3">
                            <h2 className="text-xl mb-4">Definir Quantidade</h2>
                            <div className="mb-4">
                                <label className="block text-gray-700">Quantidade</label>
                                <input
                                    type="number"
                                    min="1"
                                    value={quantidade}
                                    onChange={(e) => setQuantidade(Number(e.target.value))}
                                    className="w-full border rounded px-2 py-1"
                                />
                            </div>
                            <div className="flex justify-end space-x-4">
                                <button
                                    onClick={() => {
                                        setShowPopup(false);
                                        setQuantidade(1); // Resetar ao fechar
                                    }}
                                    className="bg-gray-500 text-white px-4 py-2 rounded"
                                >
                                    Cancelar
                                </button>
                                <button
                                    onClick={() => {
                                        handleAddRevista();
                                        setShowPopup(false);
                                    }}
                                    className="bg-blue-500 text-white px-4 py-2 rounded"
                                >
                                    Confirmar
                                </button>
                            </div>
                        </div>
                    </div>
                )}

                <div className="mb-4">
                    <label className="block text-gray-700">Status</label>
                    <select
                        value={status}
                        onChange={(e) => setStatus(e.target.value)}
                        className="w-full border rounded px-2 py-1"
                    >
                        <option value="">Selecione um STATUS</option>
                        <option value="REALIZADO">REALIZADO</option>
                        <option value="ENTREGUE">ENTREGUE</option>
                        <option value="1º PARCELA PAGA">1º PARCELA PAGA</option>
                        <option value="2º PARCELA PAGA">2º PARCELA PAGA</option>
                        <option value="1º PARCELA PENDENTE">1º PARCELA PENDENTE</option>
                        <option value="2º PARCELA PENDENTE">2º PARCELA PENDENTE</option>
                        <option value="PEDIDO COM ATRASO EM TODAS AS PARCELAS">PEDIDO COM ATRASO EM TODAS AS PARCELAS</option>
                        <option value="PEDIDO PAGO">PEDIDO PAGO</option>
                    </select>
                </div>



                <button
                    type="button"
                    onClick={handleSelectRevista}
                    className="bg-blue-500 text-white px-4 py-2 rounded"
                >
                    Adicionar Revista
                </button>

                <button
                    type="button"
                    onClick={() => setShowModal(true)}
                    className="bg-blue-500 text-white px-4 py-2 rounded ml-10"
                >
                    Ver Revistas
                </button>

                <div className="mt-4">
                    <button
                        type="submit" /* Isso dispara o onSubmit do formulário */
                        className="bg-green-500 text-white px-6 py-2 rounded hover:bg-green-700"
                    >
                        Salvar Pedido
                    </button>
                </div>
                <Button variant="primary" className='btn-primary mb-3 mt-5 flex text-end' as="a" href="/pedidos">VOLTAR</Button>
            </form>
            {/* Modal */}
                {showModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
                    <div className="bg-white p-6 rounded-lg w-4/5">
                    <h2 className="text-xl mb-4">Revistas Adicionadas</h2>
                    <ul className="space-y-2">
                        {revistasAdicionadas.map((revista, index) => (
                        <li key={index} className="flex justify-between">
                            <span>{revista.nome}</span>
                            <span>Quantidade: {revista.quantidade}</span>
                            <span>Tipo: {revista.tipo}</span>
                            <span>Formato: {revista.formato}</span>
                            <span>Total: R${revista.total.toFixed(2)}</span>
                            <button
                            onClick={() => handleRemoveRevista(index)}
                            className="text-red-500 hover:text-red-700"
                            >
                            Remover
                            </button>
                        </li>
                        ))}
                    </ul>
                    <div className="mt-4">
                        <button
                        onClick={() => setShowModal(false)}
                        className="bg-gray-500 text-white px-4 py-2 rounded"
                        >
                        Fechar
                        </button>
                    </div>
                    </div>
                </div>
            )}
        </div>
        <Footer />
    </>
    );
}

export default NewPedidos;
