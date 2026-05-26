/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useState } from "react";
import { ProfessorTurma } from "../../../types/professorTurma";
import { fetchProfessorTurmas, fetchProfessorTurmasPorIgreja } from "../../../utils/api";
import { Button, Pagination } from "react-bootstrap";
import { Link } from "react-router-dom";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import Header from "../../../components/header/header";

function ProfessorAndTurmas() {
  const [professorTurmas, setProfessorTurmas] = useState<ProfessorTurma[]>([]);
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);
  const igrejaId = sessionStorage.getItem('igrejaId');
  
  
  useEffect(() => {
    const igrejaId = sessionStorage.getItem('igrejaId');
        if (igrejaId && !isNaN(Number(igrejaId))){
        fetchProfessorTurmasPorIgreja(Number(igrejaId))
        .then(response => setProfessorTurmas(response.data))
        .catch(error => console.log(error));
        } else {
    fetchProfessorTurmas().then(response => setProfessorTurmas(response.data))
      .catch(error => console.log(error))
        }
  }, []);
  
  const handlePageChange = (page: number) => {
          setCurrentPage(page);
  };
  
  const lowerSearch = search.toLowerCase();
  const filteredProfessorTurmas = professorTurmas.filter((turma) => turma.
    nomeProfessor.toLowerCase().includes(lowerSearch));
  
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentProfessorTurmas = filteredProfessorTurmas.slice(
    indexOfFirstItem,
    indexOfLastItem
  );    
  
  
  return (
    <>
      <Header/>
      <h2 className="text-2xl font-bold mb-4 text-center">Lista Professor e Turmas</h2>
      <div className="container mx-auto px-4 w-screen h-screen">
        <div className="flex justify-between items-center">

          <input
            className="text-center border-b border-gray-200 bg-gray-100 h-10 w-56 mb-5"
            type="text"
            value={search}
            onChange={event => setSearch(event.target.value)}
            placeholder="Professor"
          />

          {(!igrejaId || igrejaId === 'null') && (
            <a href="/professorEturmas/new/0" className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md mt-12" title="NOVO">
              <BsFillPlusCircleFill className="w-8 h-8 text-white"/>
            </a>
          )}

        </div>                
  
        {/* Tabela */}
        <div className="overflow-x-auto bg-white rounded-lg shadow overflow-y-auto relative">
          <table className="border-collapse table-auto w-full whitespace-no-wrap bg-white table-striped relative">
            <thead>
              <tr className="text-left">
                <th className="py-2 px-3 sticky top-0 border-b border-gray-200 bg-gray-100"></th>
                <th className="py-2 px-3 sticky top-0 border-b border-gray-200 bg-gray-100">Nome do Professor</th>
                <th className="py-2 px-3 sticky top-0 border-b border-gray-200 bg-gray-100">Nome da Turma</th>
                  {(!igrejaId || igrejaId === 'null') && (<th className="py-2 px-3 sticky top-0 border-b border-gray-200 bg-gray-100">Ação</th>)}
              </tr>
            </thead>
            <tbody>
              {currentProfessorTurmas.map((item, index) => (
                <tr key={item.id} className="border-b border-gray-200">
                  <td className="flex justify-center p-2">{index + 1}</td>
                  <td className="py-2 px-3">{item.nomeProfessor}</td>
                  <td className="py-2 px-3">{item.nomeTurma}</td>
                  {(!igrejaId || igrejaId === 'null') && (
                  <td>
                                                          
                    <div>
                      <Link to={`/professorEturmas/${item.id}`}>
                        <button className="w-14 h-10 flex-col" title="EDITAR">
                          <BsPencil className="w-20 h-6 mt-2" color="green"/>
                        </button>
                      </Link>
                    </div>
                  </td>
                  )}
                </tr>
              ))}
            </tbody>
            
          </table>
          <Pagination className="justify-content-center mt-4">
            <Pagination.Prev
              onClick={() => handlePageChange(currentPage - 1)}
              disabled={currentPage === 1}
            />
            {[...Array(Math.ceil(filteredProfessorTurmas.length / itemsPerPage))].map(
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
              disabled={currentPage === Math.ceil(filteredProfessorTurmas.length / itemsPerPage)}
            />
          </Pagination>
        </div>

        <Button as="a" href="/professores" className="mt-4">VOLTAR</Button>
      
      </div>

    </>
  )
}

export default ProfessorAndTurmas;