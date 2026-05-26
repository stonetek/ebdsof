import { useEffect, useState } from "react";
import Table  from "react-bootstrap/Table";
import { fetchAlunoPorIgreja, fetchAlunos, fetchProfessorAluno } from "../../utils/api";
import Pagination from "react-bootstrap/Pagination";
import { Aluno } from "../../types/aluno";
import { Link } from "react-router-dom";
import { BsFillPlusCircleFill, BsPencil } from "react-icons/bs";
import { formatLocalDate } from "../../utils/format";



function DataTableAluno() {

  const [aluno, setAluno] =  useState<Aluno[]>([])
  const [search, setSearch] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);

  useEffect(() => {
  const igrejaId = sessionStorage.getItem('igrejaId');
  const classeId = sessionStorage.getItem("classeId");

  const carregarAlunos = async () => {
    try {
      let response;
      if (classeId && !isNaN(Number(classeId))) {
        response = await fetchProfessorAluno(Number(classeId));
      } else if (igrejaId && !isNaN(Number(igrejaId))) {
        response = await fetchAlunoPorIgreja(Number(igrejaId));
      } else {
        response = await fetchAlunos();
      }

      setAluno(response.data);

      // 🔹 Se há igreja definida e os alunos possuem área, armazena a área
            if (igrejaId && response.data.length > 0) {
              
              const primeiraArea = response.data[0].area;
              const todasIguais = response.data.every((a: Aluno) => a.area === primeiraArea);
      
              if (todasIguais) {
                sessionStorage.setItem("areaAtual", primeiraArea);
              } else {
                sessionStorage.removeItem("areaAtual");
              }
            }
    } catch (error) {
      console.error("Erro ao buscar alunos:", error);
    }
  };

  carregarAlunos();  
}, []);




  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };

    
  const lowerSearch = search.toLowerCase();
  const filteredAlunos = aluno.filter((aluno) => aluno.
    nome.toLowerCase().includes(lowerSearch));

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentAlunos = filteredAlunos.slice(
    indexOfFirstItem,
    indexOfLastItem
  );    



  return (
    <>
        {/* Pesquisa + botão */}
        <div className="flex flex-row md:flex-row justify-between items-center gap-3 mb-4">

            <input
                className="text-center bg-slate-500 text-white h-10 md:w-64 rounded-lg px-3 mb-5"
                type="text"
                value={search}
                onChange={event => setSearch(event.target.value)}
                placeholder="Digite nome"
            />

            <a
                href="/alunos/new/0"
                className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md mt-12"
                title="NOVO"
            >
                <BsFillPlusCircleFill className="w-8 h-8 text-white" />
            </a>

        </div>

        {/* Tabela */}
        <div className="overflow-x-auto rounded-lg shadow">

            <Table striped bordered hover variant="dark" className="min-w-[800px]">

                <thead>
                    <tr>
                        <th></th>
                        <th>Nome</th>
                        <th>Aniversário</th>
                        <th>Área</th>
                        <th>Novo Convertido</th>
                        <th>Gênero</th>
                        <th>Ação</th>
                    </tr>
                </thead>

                <tbody>
                    {currentAlunos.map((aluno, index) => (
                        <tr key={aluno.id}>

                            <td>{index + 1}</td>

                            <td>{aluno.nome}</td>

                            <td>
                                {formatLocalDate(
                                    aluno.aniversario,
                                    'dd/MM/yyyy'
                                )}
                            </td>

                            <td>{aluno.area}</td>

                            <td>
                                {aluno.novoConvertido ? 'SIM' : 'NÃO'}
                            </td>

                            <td>{aluno.sexo}</td>

                            <td>
                                <div className="flex justify-center">

                                    <Link to={`/alunos/${aluno.id}`}>
                                        <button
                                            className="w-10 h-10 flex items-center justify-center"
                                            title="EDITAR"
                                        >
                                            <BsPencil
                                                className="w-5 h-5"
                                                color="yellow"
                                            />
                                        </button>
                                    </Link>

                                </div>
                            </td>

                        </tr>
                    ))}
                </tbody>

            </Table>

        </div>

        {/* Paginação */}
        <div className="flex justify-center mt-4 overflow-x-auto">

            <Pagination>

                <Pagination.Prev
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 1}
                />

                {[...Array(Math.ceil(filteredAlunos.length / itemsPerPage))].map(
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
                        Math.ceil(filteredAlunos.length / itemsPerPage)
                    }
                />

            </Pagination>

        </div>
    </>
)

}

export default DataTableAluno;