/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/no-explicit-any */
import { SetStateAction, useEffect, useState } from "react";
import { Button, Col, FormControl, Pagination, Row, Table } from "react-bootstrap";
import { fetchProfessorAT } from "../../utils/api";
import { formatLocalDate } from "../../utils/format";
import jsPDF from 'jspdf';
import 'jspdf-autotable';


interface Professor {
    nome: string;
  }
  
  interface Turma {
    nome: string;
  }
  
  interface Aula {
    id: number;
    licao: string;
    dia: string;
    alunosMatriculados: string;
    trimestre: string;
    ausentes: string;
    presentes: string;
    visitantes: string;
    totalAssistencia: string;
    biblias: string;
    revistas: string;
    oferta: number;
    professorAulas: { id: number }[];
  }
  
  interface ProfessorData {
    [x: string]: any;
    professores: Professor[];
    turmas: Turma[];
    aulas: Aula[];
  }

function ProfessorTurma() {
    
  const [professoresData, setProfessoresData] = useState<ProfessorData[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);
  const [searchTerm, setSearchTerm] = useState('');
  const [errorMessage, setErrorMessage] = useState<string | null>(null);


  useEffect(() => {
    const userProfile = sessionStorage.getItem('userProfile');
    const userId = sessionStorage.getItem('usuario');
    const igrejaId = sessionStorage.getItem('igrejaId');
    const igrejaIdNumber = igrejaId ? Number(igrejaId) : null; 
    fetchProfessorAT(userProfile, userId, igrejaIdNumber)
    .then(response => {
      setProfessoresData(response.data);
      setErrorMessage(null);
  })
  .catch(errorMessage => {
      setErrorMessage(errorMessage.response?.data?.message || "Não há aulas cadastradas nesse perfil");
  });
  }, []);


  const handleSearchChange = (event: { target: { value: SetStateAction<string>; }; }) => {
    setSearchTerm(event.target.value);
    setCurrentPage(1);
  };

  const filteredProfessoresData = professoresData.filter(professorData => 
    professorData.professores[0]?.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = filteredProfessoresData.slice(indexOfFirstItem, indexOfLastItem);

  const totalPages = Math.ceil(filteredProfessoresData.length / itemsPerPage);

  const handlePageChange = (pageNumber: SetStateAction<number>) => {
    setCurrentPage(pageNumber);
  };

  const generatePDF = () => {
    const doc = new jsPDF();
  
    // Add title
    doc.setFontSize(18);
    doc.text("Professores com Turmas e Aulas", 105, 20, { align: 'center' });
    
    // Prepare data for table
    const tableData: string[][] = [];
    filteredProfessoresData.forEach(professorData => {
      const professorNome = professorData.professores[0]?.nome || '';
      const turmas = professorData.turmas ? professorData.turmas.map(turma => turma.nome).join(', ') : '';
      const aula = professorData.aula ? `${professorData.aula.licao} (${formatLocalDate(professorData.aula.dia, 'dd/MM/yyyy')})` : '';
      tableData.push([professorNome, turmas, aula]);
    });
  
    // Define table columns and format data
    const tableColumns = ["Professor", "Turmas", "Última Aula"];
  
    // Generate table
    doc.autoTable({
      startY: 30,
      head: [tableColumns],
      body: tableData,
      didDrawCell: (data: { row: { index: number; }; }) => {
        // Example: applying style to every second row
        if (data.row.index % 2 === 0) {
          doc.setFillColor(240, 240, 240); // Background color for even rows
          doc.setFontSize(10);
        } else {
          doc.setFillColor(255, 255, 255); // Background color for odd rows
          doc.setFontSize(10);
        }
      }
    });
  
    // Add date at the bottom
    const date = new Date();
    const formattedDate = `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
    const xPosition = 10;
    const yPosition = doc.internal.pageSize.height - 10;
    doc.setFontSize(8);
    doc.text(`Data de criação: ${formattedDate}`, xPosition, yPosition);
  
    // Save PDF
    doc.save('professores-turmas-aulas.pdf');
  };
  

  return (
    <div>
      <h2>Professores com Turmas e Aulas</h2>
      <FormControl
        type="text"
        placeholder="Pesquisar por professor"
        value={searchTerm}
        onChange={handleSearchChange}
        className="mb-3"
      />
      <Table striped bordered hover variant="dark">
        <thead>
          <tr>
            <th>Professor</th>
            <th>Turmas</th>
            <th>Aulas</th>
          </tr>
        </thead>
        <tbody>
          {currentItems.map((professorData, index) => (
            <tr key={index}>
              <td>{professorData.professores[0]?.nome}</td>
              <td>
                <ul>
                  {professorData.turmas && professorData.turmas.map((turma, index) => (
                    <li key={index}>{turma.nome}</li>
                  ))}
                </ul>
              </td>
              <td>
                <ul>
                {professorData.aula && (
                    <li>{professorData.aula.licao} ({formatLocalDate(professorData.aula.dia,'dd/MM/yyyy')})</li>
                  )}
                </ul>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      <Row className="justify-content-center">
        <Col xs="auto">
          <Pagination>
            {[...Array(totalPages).keys()].map(number => (
              <Pagination.Item key={number + 1} active={number + 1 === currentPage} onClick={() => handlePageChange(number + 1)}>
                {number + 1}
              </Pagination.Item>
            ))}
          </Pagination>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col xs="auto">
          <Button onClick={generatePDF}>Gerar PDF</Button>
          <Button variant="primary" className='ml-10' as="a" href="/home">VOLTAR</Button>
        </Col>
      </Row>
    </div>
  );
}

export default ProfessorTurma;