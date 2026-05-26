/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useState } from "react";
import { AlunoAulas } from "../../../types/alunoAula";
import { fetchAlunoAulas, fetchAlunoAulasPorIgreja } from "../../../utils/api";
import { Pagination, Button } from "react-bootstrap";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import { Link } from "react-router-dom";
import Header from "../../../components/header/header";
import Footer from "../../../components/footer/footer";

function AlunoAndAulas() {

  const [alunoAulas, setAlunoAulas] = useState<AlunoAulas[]>([]);
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);
  const igrejaId = sessionStorage.getItem('igrejaId');


  
  useEffect(() => {
        const igrejaId = sessionStorage.getItem('igrejaId');
        if (igrejaId && !isNaN(Number(igrejaId))){
          fetchAlunoAulasPorIgreja(Number(igrejaId))
            .then(response => setAlunoAulas(response.data))
            .catch(error => console.log(error));
          } else {
        fetchAlunoAulas().then(response => setAlunoAulas(response.data))
          .catch(error => console.log(error))
        }
      }, []);
  
      const handlePageChange = (page: number) => {
          setCurrentPage(page);
  };
  
  const lowerSearch = search.toLowerCase();
  const filteredAlunoAulas = alunoAulas.filter((aula) => aula.
      licao.toLowerCase().includes(lowerSearch));
  
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentAlunoAulas = filteredAlunoAulas.slice(
    indexOfFirstItem,
    indexOfLastItem
  );    
  
  
  return (
  <>
    <Header />

    <div className="container mx-auto px-3 sm:px-4 lg:px-6 mt-4 sm:mt-6 min-h-screen w-full">
        <h2 className="text-lg sm:text-2xl font-bold text-center sm:text-left">
          Lista Aluno e Aula
        </h2>

      {/* TOPO */}
      <div className="flex flex-row items-center gap-4 ">
        {/* PESQUISA */}
         <input
          className="text-center border border-gray-300 rounded-md bg-gray-100 h-10 w-full sm:w-72 md:w-80"
          type="text"
          value={search}
          onChange={event => setSearch(event.target.value)}
          placeholder="Nome Aula"
        />
        {(!igrejaId || igrejaId === 'null') && (
          <a
            href="/alunosEaulas/new/0"
            className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md hover:bg-blue-600 transition flex-shrink-0 mt-12"
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

              <th className="py-3 px-2 sm:px-3 border-b text-center">
                #
              </th>

              <th className="py-3 px-2 sm:px-3 border-b min-w-[180px]">
                Nome do Aluno
              </th>

              <th className="py-3 px-2 sm:px-3 border-b min-w-[180px]">
                Nome da Aula
              </th>

              {(!igrejaId || igrejaId === 'null') && (
                <th className="py-3 px-2 sm:px-3 border-b text-center min-w-[100px]">
                  Ação
                </th>
              )}
            </tr>
          </thead>

          <tbody>
            {currentAlunoAulas.map((item, index) => (
              <tr
                key={item.id}
                className="border-b border-gray-200 hover:bg-gray-50"
              >

                <td className="py-2 px-2 sm:px-3 text-center">
                  {index + 1}
                </td>

                <td className="py-2 px-2 sm:px-3 break-words">
                  {item.nomeAluno}
                </td>

                <td className="py-2 px-2 sm:px-3 break-words">
                  {item.licao}
                </td>

                {(!igrejaId || igrejaId === 'null') && (
                  <td className="py-2 px-2 sm:px-3">
                    <div className="flex justify-center">

                      <Link to={`/alunosEaulas/${item.id}`}>
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

            {[...Array(Math.ceil(filteredAlunoAulas.length / itemsPerPage))].map(
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
                Math.ceil(filteredAlunoAulas.length / itemsPerPage)
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

export default AlunoAndAulas;
