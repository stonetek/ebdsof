/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useState} from "react";
import { Button, Table, Pagination } from "react-bootstrap";
import { BsQrCode, BsFillPlusCircleFill } from "react-icons/bs";
import { Pagamento, parcelasSet} from "../../types/pagamento";
import Footer from "../../components/footer/footer";
import Header from "../../components/header/header";
import { formatLocalDate } from "../../utils/format";
//import api from "../../service/api";
import { fetchPagamentoPorIgreja, fetchPagamentos } from "../../utils/api";
import CODE from "../../../public/image/qrcode1.jpg"
import Menu from "../../components/menu/Menu.tsx";

function Pagamentos() {
    const [pagamento, setPagamento] = useState<Pagamento[]>([]);
    const [search, setSearch] = useState("");
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage] = useState(3);
    const [showModal, setShowModal] = useState(false);
    const [selectedParcela, setSelectedParcela] = useState<parcelasSet | null>(null);
    const [qrCodeUrl, setQrCodeUrl] = useState<string | null>(null);
    const nomePerfil = sessionStorage.getItem("nomePerfil");

    useEffect(() => {
        let isMounted = true;
        const igrejaId = sessionStorage.getItem('igrejaId');
        console.log
        const fetchData = async () => {
            try {
                let response;
                if (igrejaId && !isNaN(Number(igrejaId))) {
                    response = await fetchPagamentoPorIgreja(Number(igrejaId));
                } else {
                    response = await fetchPagamentos();
                }

                if (isMounted && response.data) {
                    const pagamentosData = response.data.map((pagamento: Pagamento) => ({
                        ...pagamento,
                        parcelasSet: pagamento.parcelasSet.map((parcela) => ({
                            ...parcela,
                        })),
                    }));

                    setPagamento(pagamentosData);
                }
            } catch (error) {
                if (isMounted) {
                    console.error("Erro ao buscar pagamentos:", error);
                }
            }
        };

        fetchData();

        return () => {
            isMounted = false;
        };
    }, []);

    const handlePageChange = (page: number) => {
        setCurrentPage(page);
    };

    const lowerSearch = search.toLowerCase();
    const filteredPagamento = pagamento.filter((p) =>
        p.status.toLowerCase().includes(lowerSearch)
    );

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentPagamentos = filteredPagamento.slice(indexOfFirstItem, indexOfLastItem);
    
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    const handleQrCode = async (pagamento: Pagamento, parcela: parcelasSet) => {
        try {
            // URL da imagem do QR code armazenada na pasta public/images
            <img src={CODE} alt="Avatar" className="w-10 h-10 rounded-full mr-2" />
    
            // Fetch the QR code image
            const response = await fetch(CODE);
            const blob = await response.blob();
            const qrCodeUrl = URL.createObjectURL(blob);
    
            // Atualiza o estado
            setSelectedParcela(parcela);
            setQrCodeUrl(qrCodeUrl);
            setShowModal(true);
        } catch (error) {
            console.error("Erro ao buscar o QR Code:", error);
            alert("Erro ao buscar o QR Code. Verifique a URL e tente novamente.");
        }
    };
    
    
    // Limpar URL para evitar vazamento de memória
    useEffect(() => {
        return () => {
            if (qrCodeUrl) {
                URL.revokeObjectURL(qrCodeUrl);
            }
        };
    }, [qrCodeUrl]);

    function calcularAtraso(dataVencimento: string, dataPagamento: string | null): number {
        if (dataPagamento) return 0;
        const hoje = new Date();
        const vencimento = new Date(dataVencimento);
        const diff = hoje.getTime() - vencimento.getTime();
        const dias = Math.floor(diff / (1000 * 60 * 60 * 24));
        return dias > 0 ? dias : 0;
    }
    

    return (
    <>
        <Header />

        {nomePerfil !== "admin_igreja" && <Menu />}

        <div className="min-h-screen flex flex-col px-4 py-6 md:px-6 lg:px-8">

            {/* Busca + botão */}
            <div className="flex justify-between items-center gap-4 mb-4 flex-wrap">

                <input
                    className="text-center bg-slate-500 text-white h-10 sm:w-72 rounded-lg px-3"
                    type="text"
                    value={search}
                    onChange={(event) =>
                        setSearch(event.target.value)
                    }
                    placeholder="Digite o status do pagamento"
                />

                <a
                    href="/pedidos/new/0"
                    className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md shrink-0 mt-12"
                    title="NOVO PEDIDO"
                >
                    <BsFillPlusCircleFill className="w-8 h-8 text-white" />
                </a>

            </div>

            {/* Tabela */}
            <div className="overflow-x-auto rounded-xl shadow-md">

                <Table
                    responsive
                    striped
                    bordered
                    hover
                    variant="dark"
                    className="min-w-[1000px] mb-0"
                >

                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Total</th>
                            <th>Status</th>
                            <th>Parcelas</th>
                        </tr>
                    </thead>

                    <tbody>

                        {currentPagamentos.map(
                            (pagamento, index) => (

                                <tr key={pagamento.id}>

                                    <td>{index + 1}</td>

                                    <td>
                                        {pagamento.valorTotal.toLocaleString(
                                            "pt-BR",
                                            {
                                                style: "currency",
                                                currency: "BRL",
                                            }
                                        )}
                                    </td>

                                    <td>
                                        {pagamento.status}
                                    </td>

                                    {/* Parcelas */}
                                    <td>

                                        <div className="flex flex-wrap gap-4">

                                            {pagamento.parcelasSet.map(
                                                (parcela) => {

                                                    const atraso =
                                                        calcularAtraso(
                                                            parcela.dataVencimento,
                                                            parcela.dataPagamento
                                                        );

                                                    const statusColor =
                                                        String(parcela.status) === "PAGO"
                                                            ? "text-green-400"
                                                            : atraso > 0
                                                            ? "text-red-400"
                                                            : "text-blue-300";

                                                    const isPago =
                                                        String(parcela.status) === "PAGO";

                                                    return (

                                                        <div
                                                            key={parcela.id}
                                                            className="border border-gray-600 rounded-lg p-3 min-w-[220px] bg-slate-800"
                                                        >

                                                            <div className="text-sm">

                                                                <div>
                                                                    <strong>
                                                                        Parcela {parcela.numero}
                                                                    </strong>
                                                                </div>

                                                                <div>
                                                                    {
                                                                        parcela.valor.toLocaleString(
                                                                            "pt-BR",
                                                                            {
                                                                                style: "currency",
                                                                                currency: "BRL",
                                                                            }
                                                                        )
                                                                    }
                                                                </div>

                                                                <div>
                                                                    Vencimento:{" "}
                                                                    {
                                                                        formatLocalDate(
                                                                            parcela.dataVencimento,
                                                                            "dd/MM/yyyy"
                                                                        )
                                                                    }
                                                                </div>

                                                                <div>

                                                                    {parcela.dataPagamento ? (
                                                                        <span>
                                                                            Pago
                                                                        </span>
                                                                    ) : atraso > 0 ? (
                                                                        <span>
                                                                            Atraso: {atraso} dias
                                                                        </span>
                                                                    ) : (
                                                                        <span>
                                                                            A vencer
                                                                        </span>
                                                                    )}

                                                                </div>

                                                                <div
                                                                    className={`font-bold ${statusColor}`}
                                                                >
                                                                    Situação: {parcela.status}
                                                                </div>

                                                            </div>

                                                            {/* QRCode */}
                                                            {!parcela.dataPagamento && (

                                                                <Button
                                                                    onClick={() =>
                                                                        handleQrCode(
                                                                            pagamento,
                                                                            parcela
                                                                        )
                                                                    }
                                                                    title="Gerar QR Code"
                                                                    disabled={isPago}
                                                                    className="mt-3 flex items-center gap-2"
                                                                    variant="light"
                                                                >

                                                                    <BsQrCode />

                                                                    QR Code

                                                                </Button>

                                                            )}

                                                        </div>
                                                    );
                                                }
                                            )}

                                        </div>

                                    </td>

                                </tr>
                            )
                        )}

                    </tbody>

                </Table>

            </div>

            {/* Paginação */}
            <div className="flex justify-center mt-4 overflow-x-auto">

                <Pagination>

                    <Pagination.Prev
                        onClick={() =>
                            handlePageChange(currentPage - 1)
                        }
                        disabled={currentPage === 1}
                    />

                    {[...Array(
                        Math.ceil(
                            filteredPagamento.length /
                            itemsPerPage
                        )
                    )].map((_, index) => (

                        <Pagination.Item
                            key={index}
                            active={index + 1 === currentPage}
                            onClick={() =>
                                handlePageChange(index + 1)
                            }
                        >
                            {index + 1}
                        </Pagination.Item>

                    ))}

                    <Pagination.Next
                        onClick={() =>
                            handlePageChange(currentPage + 1)
                        }
                        disabled={
                            currentPage ===
                            Math.ceil(
                                filteredPagamento.length /
                                itemsPerPage
                            )
                        }
                    />

                </Pagination>

            </div>

            {/* Botão voltar */}
            <div className="flex w-full mt-4">

                <Button
                    variant="primary"
                    className="btn-primary"
                    as="a"
                    href="/pedidos"
                >
                    VOLTAR
                </Button>

            </div>

        </div>

        {/* Modal QRCode */}
        {showModal && (

            <div className="fixed inset-0 flex items-center justify-center bg-black/60 z-50 px-4">

                <div className="bg-white p-6 rounded-2xl shadow-xl text-center max-w-sm w-full">

                    <h2 className="text-xl font-bold mb-4">
                        QR Code - Parcela {selectedParcela?.numero}
                    </h2>

                    <p className="text-lg mb-4">

                        Valor:{" "}

                        {selectedParcela?.valor.toLocaleString(
                            "pt-BR",
                            {
                                style: "currency",
                                currency: "BRL",
                            }
                        )}

                    </p>

                    {qrCodeUrl && (

                        <img
                            src={qrCodeUrl}
                            alt="QR Code de Pagamento"
                            className="w-56 h-56 mx-auto"
                        />

                    )}

                    <button
                        className="mt-5 bg-red-500 hover:bg-red-600 transition text-white px-4 py-2 rounded-lg"
                        onClick={() => setShowModal(false)}
                    >
                        Fechar
                    </button>

                </div>

            </div>
        )}

        <Footer />
    </>
);
}

export default Pagamentos;