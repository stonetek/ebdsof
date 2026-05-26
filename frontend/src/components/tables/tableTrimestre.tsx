import React, { useState, useEffect } from 'react';
import { Table, Form, Button, Pagination, Alert } from 'react-bootstrap';
import { formatLocalDate } from '../../utils/format';
import Header from '../../components/header/header';
import { BsFillPlusCircleFill } from 'react-icons/bs';
import { Aula } from '../../types/aula';
import Footer from '../footer/footer';
import { fetchAulas, fetchAulasPorIgreja } from '../../utils/api';



function DataTableTrimestre() {
    const [aulas, setAulas] = useState<Aula[]>([]);
    const [trimestre, setTrimestre] = useState('');
    const [ano, setAno] = useState<number>(0);
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage] = useState(5);
    const [turma, setTurma] = useState('');
    const [filteredAulas, setFilteredAulas] = useState<Aula[]>([]);
    


    useEffect(() => {
        const igrejaId = sessionStorage.getItem('igrejaId'); 
        if (igrejaId && !isNaN(Number(igrejaId))) {
          fetchAulasPorIgreja(Number(igrejaId))
            .then(response => {
              setAulas(response.data);
            })
            .catch(error => console.log(error));
        } else {
            fetchAulas().then(response => setAulas(response.data))
            .catch(error => console.log(error))
            
        }
      }, []);

    const handlePageChange = (page: React.SetStateAction<number>) => {
        setCurrentPage(page);
    };

    const handleSearch = () => {
        const result = aulas.filter((aula) => {
            const aulaAno = new Date(aula.dia).getFullYear();
            const normalizedTrimestre = trimestre.trim().toLowerCase();
            const normalizedAulaTrimestre = aula.trimestre.trim().toLowerCase();
            const matchesTrimestre = (trimestre === '' || normalizedAulaTrimestre === normalizedTrimestre);
            const matchesAno = (ano === 0 || aulaAno === ano);
            const matchesTurma = (turma === '' || aula.aulasTurmas?.some(t => t.nomeTurma.toLowerCase() === turma.toLowerCase()));
    
            return matchesTrimestre && matchesAno && matchesTurma;
        });
    
        setFilteredAulas(result); // Atualiza o estado com o resultado filtrado
        setCurrentPage(1); // Opcional, reinicia a paginação
    };
    

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentAulas = filteredAulas.slice(indexOfFirstItem, indexOfLastItem);
    

   return (
  <>
    <Header/>

    <div className="w-screen min-h-screen p-5">
            
      {/* Filtros e botões */}
        <div className="flex flex-col md:flex-row items-center md:items-start mt-20 mb-5 ml-0 md:ml-5 rounded-3xl gap-3 md:gap-2">
            <Form.Control
            as="select"
            value={trimestre}
            onChange={(e) => setTrimestre(e.target.value)}
            placeholder="Selecione o trimestre"
            className="w-full md:w-auto"
            >
            <option value="">Selecione o trimestre</option>
            <option value="1º trimestre">1º Trimestre</option>
            <option value="2º trimestre">2º Trimestre</option>
            <option value="3º trimestre">3º Trimestre</option>
            <option value="4º trimestre">4º Trimestre</option>
            </Form.Control>

            <Form.Control
            type="number"
            value={ano}
            onChange={(e) => setAno(parseInt(e.target.value))}
            placeholder="Digite o ano"
            className="w-full md:w-auto"
            />

            <Form.Control
            as="select"
            value={turma}
            onChange={(e) => setTurma(e.target.value)}
            placeholder="Selecione a turma"
            className="w-full md:w-auto"
            >
            <option value="">Selecione a turma</option>
            {[...new Set(aulas.flatMap(aula => aula.aulasTurmas || []).map(t => t.nomeTurma))].map((nomeTurma, index) => (
                <option key={index} value={nomeTurma}>{nomeTurma}</option>
            ))}
            </Form.Control>

             {/* Agrupando Buscar e Novo */}
            <div className="flex flex-row gap-3 w-full md:w-auto">
                <Button variant="primary" onClick={handleSearch} className="flex-1">
                Buscar
                </Button>

                <a
                href="/aulas/new/0"
                className="flex items-center justify-center w-12 h-12 bg-blue-500 rounded-full shadow-md mt-12"
                title="NOVO"
                >
                <BsFillPlusCircleFill className="w-8 h-8 text-white"/>
                </a>
            </div>
        </div>

      {/* Tabela responsiva */}
      <div className="overflow-x-auto">
        {currentAulas.length === 0 ? (
          <Alert variant="warning">Nenhuma aula encontrada</Alert>
        ) : (
          <Table striped bordered hover variant="dark" className="min-w-full">
            <thead>
              <tr>
                <th>#</th>
                <th>Trimestre</th>
                <th>Data</th>
                <th>Lição</th>
                <th>Professor</th>
                <th>Alunos Matriculados</th>
                <th>Presentes</th>
                <th>Ausentes</th>
                <th>Visitantes</th>
                <th>Total de assistência</th>
                <th>Bíblias</th>
                <th>Revistas</th>
                <th>Oferta</th>
                <th>Turmas</th>
              </tr>
            </thead>
            <tbody>
              {currentAulas.map((aula, index) => (
                <tr key={aula.id}>
                  <td>{index + 1}</td>
                  <td>{aula.trimestre}</td>
                  <td>{formatLocalDate(aula.dia, "dd/MM/yyyy")}</td>
                  <td>{aula.licao}</td>
                  <td>
                    {aula.professorAulas.map((professor, index) => (
                      <div key={index}>{professor.nomeProfessor}</div>
                    ))}
                  </td>
                  <td>{aula.alunosMatriculados}</td>
                  <td>{aula.presentes}</td>
                  <td>{aula.ausentes}</td>
                  <td>{aula.visitantes}</td>
                  <td>{aula.totalAssistencia}</td>
                  <td>{aula.biblias}</td>
                  <td>{aula.revistas}</td>
                  <td>{aula.oferta.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}</td>
                  <td>
                    {aula.aulasTurmas.map((turma, index) => (
                      <div key={index}>{turma.nomeTurma}</div>
                    ))}
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        )}
      </div>

      {/* Paginação */}
      <Pagination className="flex justify-center p-2 flex-wrap">
        <Pagination.Prev
          onClick={() => handlePageChange(currentPage - 1)}
          disabled={currentPage === 1}
        />
        {[...Array(Math.ceil(filteredAulas.length / itemsPerPage))].map((_, index) => (
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
          disabled={currentPage === Math.ceil(filteredAulas.length / itemsPerPage)}
        />
      </Pagination>

      <div className="text-start">
        <Button variant="primary" className="btn-primary mb-3 mt-12 text-slate-100" as="a" href="/home">
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

export default DataTableTrimestre;
