/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useState } from "react";
import { AlunoTurmas } from "../../../types/alunoTurmas";
import { fetchAlunoTurmas, fetchAlunoTurmasPorIgreja } from "../../../utils/api";
import Header from "../../../components/header/header";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import { Link } from "react-router-dom";
import { Button, Pagination } from "react-bootstrap";
import Footer from "../../../components/footer/footer";

function AlunoAndTurmas() {

    const [alunoTurmas, setAlunoTurmas] = useState<AlunoTurmas[]>([]);
    const [search, setSearch] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage] = useState(5);
    const igrejaId = sessionStorage.getItem('igrejaId');
  
  
    useEffect(() => {
      const igrejaId = sessionStorage.getItem('igrejaId');
      if (igrejaId && !isNaN(Number(igrejaId))){
        fetchAlunoTurmasPorIgreja(Number(igrejaId))
        .then(response => setAlunoTurmas(response.data))
        .catch(error => console.log(error));
        } else {
        fetchAlunoTurmas().then(response => setAlunoTurmas(response.data))
          .catch(error => console.log(error))
        }
    }, []);
  
    
    const handlePageChange = (page: number) => {
          setCurrentPage(page);
    };
  
    const lowerSearch = search.toLowerCase();
    const filteredAlunoTurmas = alunoTurmas.filter((turma) => turma.
      nomeTurma.toLowerCase().includes(lowerSearch));
  
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentAlunoTurmas = filteredAlunoTurmas.slice(
      indexOfFirstItem,
      indexOfLastItem
    );    
  
  
     return (
  <>
    <Header />

    <div className="container mx-auto px-3 sm:px-4 lg:px-6 mt-4 sm:mt-8 min-h-screen w-full">
        <h2 className="text-lg sm:text-2xl font-bold mt-3 sm:mt-5 text-center sm:text-left">
          Matrícula de Alunos em Turmas
        </h2>

      {/* TOPO */}
      <div className="flex flex-row items-center gap-4 w-full mt-4">
        {/* PESQUISA */}
        <input
          className="text-center border border-gray-300 rounded-md bg-gray-100 h-10 w-full sm:w-72 md:w-80 px-2"
          type="text"
          value={search}
          onChange={(event) => setSearch(event.target.value)}
          placeholder="Nome Turma"
        />

        {/* BOTÃO NOVO */}
        {(!igrejaId || igrejaId === 'null') && (
          <a
            href="/alunosEturmas/new/0"
            className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md hover:bg-blue-600 transition mt-12"
            title="NOVO"
          >
            <BsFillPlusCircleFill className="w-8 h-8 text-white" />
          </a>
        )}
      </div>


      {/* TABELA */}
      <div className="overflow-x-auto bg-white rounded-lg shadow mt-5">

        <table className="min-w-full border-collapse text-sm sm:text-base">

          <thead>
            <tr className="text-left bg-gray-100">

              <th className="py-3 px-2 sm:px-3 border-b">
                #
              </th>

              <th className="py-3 px-2 sm:px-3 border-b min-w-[140px]">
                Nome da Turma
              </th>

              <th className="py-3 px-2 sm:px-3 border-b min-w-[180px]">
                Nome do Aluno
              </th>

              {(!igrejaId || igrejaId === 'null') && (
                <th className="py-3 px-2 sm:px-3 border-b text-center min-w-[100px]">
                  Ação
                </th>
              )}
            </tr>
          </thead>

          <tbody>
            {currentAlunoTurmas.map((item, index) => (
              <tr
                key={item.id}
                className="border-b border-gray-200 hover:bg-gray-50"
              >
                <td className="py-2 px-2 sm:px-3 text-center">
                  {index + 1}
                </td>

                <td className="py-2 px-2 sm:px-3 break-words">
                  {item.nomeTurma}
                </td>

                <td className="py-2 px-2 sm:px-3 break-words">
                  {item.nomeAluno}
                </td>

                {(!igrejaId || igrejaId === 'null') && (
                  <td className="py-2 px-2 sm:px-3">
                    <div className="flex justify-center">
                      <Link to={`/editarAlunosEturmas/${item.id}`}>
                        <button
                          className="w-10 h-10 flex items-center justify-center rounded hover:bg-gray-100 transition"
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

        {/* PAGINAÇÃO */}
        <div className="flex justify-center overflow-x-auto py-4 px-2">
          <Pagination className="flex flex-wrap justify-center gap-1">

            <Pagination.Prev
              onClick={() => handlePageChange(currentPage - 1)}
              disabled={currentPage === 1}
            />

            {[...Array(Math.ceil(filteredAlunoTurmas.length / itemsPerPage))].map(
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
                Math.ceil(filteredAlunoTurmas.length / itemsPerPage)
              }
            />
          </Pagination>
        </div>
      </div>

      {/* BOTÃO VOLTAR */}
      <div className="mt-4">
        <Button as="a" href="/alunos">
          VOLTAR
        </Button>
      </div>
    </div>

    <footer className="mt-8">
      <Footer />
    </footer>
  </>
)
  }
  
  export default AlunoAndTurmas;