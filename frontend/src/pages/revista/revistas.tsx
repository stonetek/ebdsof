import { useEffect, useState } from "react";
import { Revista } from "../../types/revista";
import Header from "../../components/header/header";
import Footer from "../../components/footer/footer";
import { fetchRevistas, fetchRevistasPorIgreja } from "../../utils/api";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import { Button, Pagination, Table } from "react-bootstrap";
import { Link } from "react-router-dom";

function Revistas() {

  const [ revista, setRevista] = useState<Revista[]>([]);
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);


  useEffect(() => {
    const igrejaId = sessionStorage.getItem('igrejaId');
      if (igrejaId && !isNaN(Number(igrejaId))){
        fetchRevistasPorIgreja(Number(igrejaId))
          .then(response => setRevista(response.data))
          .catch(error => console.log(error));
        } else {
        fetchRevistas().then(response => setRevista(response.data))
        .catch(error => console.log(error))
      }
  }, []);

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };
    
        
  const lowerSearch = search.toLowerCase();
  const filteredRevistas = revista.filter((revista) => revista.
  nome.toLowerCase().includes(lowerSearch));
    
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentRevistas = filteredRevistas.slice(
    indexOfFirstItem,
    indexOfLastItem
  );    

return(
  <>
    <Header/>
    <h5 className="text-center font-bold md:text-xl lg:text-2xl">Lista de Revistas</h5>
    
    <div className="p-5">  
      {/* Filtros e botão novo */}
      <div className="flex flex-row sm:flex-row justify-between items-center mb-5 rounded-3xl w-full p-4 sm:p-10 gap-4">
        
        {/* Input de busca */}
        <div className="flex justify-start w-full sm:w-auto">
          <input
            className="text-center bg-slate-500 h-8 w-full sm:w-56"
            type="text"
            value={search}
            onChange={event => setSearch(event.target.value)}
            placeholder="digite nome"
          />
        </div>

        {/* Botão Novo */}
        <a 
          href="/revistas/new/0" 
          className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md mt-12"
          title="NOVO"
        >
          <BsFillPlusCircleFill className="w-8 h-8 text-white"/>
        </a>
      </div>

      {/* Tabela responsiva */}
      <div className="overflow-x-auto">
        <Table striped bordered hover variant="dark" className="min-w-full">
          <thead>
            <tr>
              <th>#</th>
              <th>Nome</th>
              <th>Formato</th>
              <th>Tipo</th>
              <th>Valor</th>
              <th>Ação</th>
            </tr>
          </thead>
          <tbody>
            {currentRevistas.map((revista, index) => (
              <tr key={revista.id}>
                <td>{index + 1}</td>
                <td>{revista.nome}</td>
                <td>{revista.formato}</td>
                <td>{revista.tipo}</td>
                <td>{revista.preco.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}</td>
                <td>
                  <Link to={`/revistas/${revista.id}`}>
                    <button className="w-14 h-10 flex items-center justify-center" title="EDITAR">
                      <BsPencil className="w-6 h-6 text-yellow-400"/>
                    </button> 
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
      
      {/* Paginação + voltar */}
      <div className="flex flex-col items-center text-center mt-6">    
        <Pagination className="flex flex-wrap justify-center">
          <Pagination.Prev
            onClick={() => handlePageChange(currentPage - 1)}
            disabled={currentPage === 1}
          />
          {[...Array(Math.ceil(filteredRevistas.length / itemsPerPage))].map((_, index) => (
            <Pagination.Item
              key={index}
              active={index + 1 === currentPage}
              onClick={() => handlePageChange(index + 1)}
            >
              {index + 1}
            </Pagination.Item>
          ))}
          <Pagination.Next
            onClick={() => handlePageChange(currentPage + 1)}
            disabled={currentPage === Math.ceil(filteredRevistas.length / itemsPerPage)}
          />
        </Pagination>

      </div>
      <Button variant="primary" className="btn-primary mb-3 mt-6" as="a" href="/home">
        VOLTAR
      </Button>
    </div>  
    
    <footer className="w-full">
      <Footer/>
    </footer>
  </>
)

}

export default Revistas;