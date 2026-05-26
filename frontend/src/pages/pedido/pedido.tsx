import { useEffect, useState } from "react";
import Footer from "../../components/footer/footer";
import Header from "../../components/header/header";
import { Pedido } from "../../types/pedido";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import { fetchPedido, fetchPedidoPorIgreja } from "../../utils/api";
import { Button, Modal, Pagination, Table } from "react-bootstrap";
import { formatLocalDate } from "../../utils/format";
import Menu from "../../components/menu/Menu";
import { Revista } from "../../types/revista";


function Pedidos() {
  const [pedido, setPedido] = useState<Pedido[]>([]);
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);
  const [showModal, setShowModal] = useState(false);
  const [selectedRevistas, setSelectedRevistas] = useState<Revista[]>([]);
  


  useEffect(() => {
  const igrejaId = sessionStorage.getItem('igrejaId');
  if (igrejaId && !isNaN(Number(igrejaId))) {
    fetchPedidoPorIgreja(Number(igrejaId))
      .then(response => {
        setPedido(Array.isArray(response.data) ? response.data : []);
      })
      .catch(error => console.log(error));
        } else {
            fetchPedido()
            .then(response => setPedido(Array.isArray(response.data) ? response.data : []))
            .catch(error => console.log(error));
        }
    }, []);


  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };
    
        
  const lowerSearch = search.toLowerCase();
  const filteredPedidos = pedido.filter((pedido) => pedido.
  nome.toLowerCase().includes(lowerSearch));
    
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentPedidos = filteredPedidos.slice(
    indexOfFirstItem,
    indexOfLastItem
  );    

  const handleShowRevistas = (revistas: Revista[]) => {
    if (Array.isArray(revistas)) {
      setSelectedRevistas(revistas);
      //setShowModal(true);
    } else {
      setSelectedRevistas([]);
    }
    setShowModal(true);
  };

  const handleCloseModal = () => setShowModal(false);

   return (
    <>
        <Header />
        <Menu />

        <div className="min-h-screen flex flex-col px-4 py-6 md:px-6 lg:px-8">

            {/* Busca + botão */}
            <div className="flex justify-between items-center gap-4 mb-4 flex-wrap">

                <input
                    className="text-center bg-slate-500 text-white h-10 sm:w-64 rounded-lg px-3"
                    type="text"
                    value={search}
                    onChange={event => setSearch(event.target.value)}
                    placeholder="Digite nome"
                />

                <a
                    href="/pedidos/new/0"
                    className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md shrink-0 mt-12"
                    title="NOVO"
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
                    className="min-w-[1300px] mb-0"
                >

                    <thead>
                        <tr>
                            <th></th>
                            <th>Nome</th>
                            <th>Data do Pedido</th>
                            <th>Entrega Prevista</th>
                            <th>Igreja</th>
                            <th>Descrição</th>
                            <th>Total Revistas</th>
                            <th>Total Pedido</th>
                            <th>Situação</th>
                            <th>Revistas</th>
                            <th>Ação</th>
                        </tr>
                    </thead>

                    <tbody>

                        {currentPedidos.map((pedido, index) => {

                            const quantidadeTotalRevistas =
                                Array.isArray(pedido.revistas)
                                    ? pedido.revistas.reduce(
                                        (total, revista) =>
                                            total + revista.quantidade,
                                        0
                                    )
                                    : 0;

                            return (

                                <tr key={pedido.id}>

                                    <td>{index + 1}</td>

                                    <td>{pedido.nome}</td>

                                    <td>
                                        {formatLocalDate(
                                            pedido.dataPedido,
                                            "dd/MM/yyyy"
                                        )}
                                    </td>

                                    <td>
                                        {formatLocalDate(
                                            pedido.dataEntregaPrevista,
                                            "dd/MM/yyyy"
                                        )}
                                    </td>

                                    <td>{pedido.igrejaNome}</td>

                                    <td>{pedido.descricao}</td>

                                    <td>{quantidadeTotalRevistas}</td>

                                    <td>
                                        {pedido.total.toLocaleString(
                                            'pt-BR',
                                            {
                                                style: 'currency',
                                                currency: 'BRL'
                                            }
                                        )}
                                    </td>

                                    <td>{pedido.status}</td>

                                    {/* Revistas */}
                                    <td className="text-center">

                                        <button
                                            title="Ver Revistas"
                                            onClick={() =>
                                                handleShowRevistas(
                                                    pedido?.revistas || []
                                                )
                                            }
                                            className="text-blue-300 font-bold hover:scale-110 transition"
                                        >
                                            📚
                                        </button>

                                    </td>

                                    {/* Editar */}
                                    <td>

                                        <div className="flex justify-center">

                                            <button
                                                className="w-10 h-10 flex items-center justify-center"
                                                title="EDITAR"
                                                onClick={() =>
                                                    window.location.href =
                                                    `/pedidos/${pedido.id}`
                                                }
                                            >

                                                <BsPencil
                                                    className="w-5 h-5"
                                                    color="yellow"
                                                />

                                            </button>

                                        </div>

                                    </td>

                                </tr>
                            );
                        })}

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
                            filteredPedidos.length / itemsPerPage
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
                                filteredPedidos.length / itemsPerPage
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
                    href="/home"
                >
                    VOLTAR
                </Button>

            </div>

        </div>

        {/* Modal */}
        <Modal
            show={showModal}
            onHide={handleCloseModal}
            centered
        >

            <Modal.Header closeButton>

                <Modal.Title>
                    Lista de Revistas
                </Modal.Title>

            </Modal.Header>

            <Modal.Body>

                <div className="flex flex-col gap-3">

                    {selectedRevistas.map((revista, index) => (

                        <div
                            key={index}
                            className="border rounded-lg p-3 shadow-sm"
                        >

                            <strong>{revista.nome}</strong>

                            <div>
                                {revista.tipo}
                            </div>

                            <div>
                                Quantidade: {revista.quantidade}
                            </div>

                            <div>
                                Formato: {revista.formato}
                            </div>

                            <div>
                                Preço: {
                                    revista.preco.toLocaleString(
                                        'pt-BR',
                                        {
                                            style: 'currency',
                                            currency: 'BRL'
                                        }
                                    )
                                }
                            </div>

                        </div>

                    ))}

                </div>

            </Modal.Body>

            <Modal.Footer>

                <Button
                    variant="secondary"
                    onClick={handleCloseModal}
                >
                    Fechar
                </Button>

            </Modal.Footer>

        </Modal>

        <Footer />

    </>
)
}

export default Pedidos;