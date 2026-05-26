/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useState } from "react";
import { AulaTurma } from "../../../types/aulaTurma";
import { fetchAulaTurmas, fetchAulaTurmasPorIgreja } from "../../../utils/api";
import Header from "../../../components/header/header";
import { Pagination, Button } from "react-bootstrap";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import { Link } from "react-router-dom";
import Footer from "../../../components/footer/footer";

function AulaAndTurmas() {

    const [aulaTurmas, setAulaTurmas] = useState<AulaTurma[]>([]);
    const [search, setSearch] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage] = useState(5);
    const igrejaId = sessionStorage.getItem('igrejaId');
  
  
    useEffect(() => {
        const igrejaId = sessionStorage.getItem('igrejaId');
        if (igrejaId && !isNaN(Number(igrejaId))) {
            fetchAulaTurmasPorIgreja(Number(igrejaId))
                .then(response => setAulaTurmas(response.data))
                .catch(error => console.log(error));
        } else {
            fetchAulaTurmas()
                .then(response => setAulaTurmas(response.data))
                .catch(error => console.log(error));
        }
    }, []);
    
  
    const handlePageChange = (page: number) => {
          setCurrentPage(page);
    };
  
    const lowerSearch = search.toLowerCase();
    const filteredAulaTurmas = aulaTurmas.filter((aula) => {
        return aula && aula.licao && aula.licao.toLowerCase().includes(lowerSearch);
    });
  
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentAulaTurmas = filteredAulaTurmas.slice(
      indexOfFirstItem,
      indexOfLastItem
    );    
  
  
      return (
        <>
            <Header />

            <div className="min-h-screen bg-slate-100 px-4 py-6">

                {/* Título */}
                <h2 className="text-2xl font-bold text-center mb-6">
                    Lista Aula e Turmas
                </h2>

                {/* Barra superior */}
                <div className="flex justify-between items-center w-full mb-4 gap-4">

                    {/* Busca */}
                    <input
                        className="text-center border border-gray-300 bg-white h-10 w-64 rounded-lg px-3 shadow-sm"
                        type="text"
                        value={search}
                        onChange={event => setSearch(event.target.value)}
                        placeholder="Nome Aula"
                    />

                    {/* Botão novo */}
                    {(!igrejaId || igrejaId === 'null') && (
                        <a
                            href="/aulasEturmas/new/0"
                            className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md shrink-0 mt-12"
                            title="NOVO"
                        >
                            <BsFillPlusCircleFill className="w-8 h-8 text-white" />
                        </a>
                    )}

                </div>

                {/* Tabela */}
                <div className="overflow-x-auto bg-white rounded-xl shadow-md">

                    <table className="table-auto w-full min-w-[700px] border-collapse">

                        <thead>
                            <tr className="text-left bg-gray-100">

                                <th className="py-3 px-3 border-b"></th>

                                <th className="py-3 px-3 border-b">
                                    Título da lição
                                </th>

                                <th className="py-3 px-3 border-b">
                                    Nome da Turma
                                </th>

                                {(!igrejaId || igrejaId === 'null') && (
                                    <th className="py-3 px-3 border-b">
                                        Ação
                                    </th>
                                )}

                            </tr>
                        </thead>

                        <tbody>
                            {currentAulaTurmas.map((item, index) => (
                                <tr
                                    key={item.id}
                                    className="border-b border-gray-200 hover:bg-gray-50"
                                >

                                    <td className="text-center py-2 px-3">
                                        {index + 1}
                                    </td>

                                    <td className="py-2 px-3">
                                        {item.licao}
                                    </td>

                                    <td className="py-2 px-3">
                                        {item.nomeTurma}
                                    </td>

                                    {(!igrejaId || igrejaId === 'null') && (
                                        <td className="py-2 px-3">

                                            <div className="flex justify-center">

                                                <Link to={`/aulasEturmas/${item.id}`}>

                                                    <button
                                                        className="w-10 h-10 flex items-center justify-center"
                                                        title="EDITAR"
                                                    >
                                                        <BsPencil
                                                            className="w-5 h-5"
                                                            color="green"
                                                        />
                                                    </button>

                                                </Link>

                                            </div>

                                        </td>
                                    )}

                                </tr>
                            ))}
                        </tbody>

                    </table>

                </div>

                {/* Paginação */}
                <div className="flex justify-center mt-4 overflow-x-auto">

                    <Pagination>

                        <Pagination.Prev
                            onClick={() => handlePageChange(currentPage - 1)}
                            disabled={currentPage === 1}
                        />

                        {[...Array(Math.ceil(filteredAulaTurmas.length / itemsPerPage))].map(
                            (_, index) => (
                                <Pagination.Item
                                    key={index}
                                    active={index + 1 === currentPage}
                                    onClick={() => handlePageChange(index + 1)}
                                >
                                    {index + 1}
                                </Pagination.Item>
                            )
                        )}

                        <Pagination.Next
                            onClick={() => handlePageChange(currentPage + 1)}
                            disabled={
                                currentPage ===
                                Math.ceil(filteredAulaTurmas.length / itemsPerPage)
                            }
                        />

                    </Pagination>

                </div>

                {/* Botão voltar */}
                <div className="mt-4">

                    <Button
                        as="a"
                        href="/aulas"
                    >
                        VOLTAR
                    </Button>

                </div>

            </div>
            <footer>
                <Footer />
            </footer>
        </>
    );

}

export default AulaAndTurmas;