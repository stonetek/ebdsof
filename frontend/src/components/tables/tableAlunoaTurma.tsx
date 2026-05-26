/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/no-explicit-any */
import html2canvas from "html2canvas";
import jsPDF from "jspdf";
import { useState, useEffect, useRef, SetStateAction } from "react";
import { Container, FormControl, Table, Row, Col, Pagination, Button } from "react-bootstrap";
import { fetchProfessorAT } from "../../utils/api";
import { formatLocalDate } from "../../utils/format";
import 'jspdf-autotable';

interface Aluno {
    nome: string;
    aniversario: string;
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
    presentes: number;
    visitantes: string;
    totalAssistencia: string;
    biblias: string;
    revistas: string;
    oferta: number;
  }
  
  interface AlunoData {
    [x: string]: any;
    alunos: Aluno[];
    turmas: Turma[];
    aulas: Aula[];
  }

function AlunoTurma() {
  const [alunosData, setAlunosData] = useState<AlunoData[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);
  const [searchTerm, setSearchTerm] = useState('');
  const pdfRef = useRef(null);

  useEffect(() => {
    fetchAlunosComDetalhes();
  }, []);

  const fetchAlunosComDetalhes = () => {
    fetchProfessorAT()
      .then(response => setAlunosData(response.data))
      .catch(error => console.log(error));
  };

  const handleSearchChange = (event: { target: { value: SetStateAction<string>; }; }) => {
    setSearchTerm(event.target.value);
    setCurrentPage(1); // Reset to the first page on new search
  };

  const filteredAlunosData = alunosData.filter(alunoData =>
    alunoData.alunos[0]?.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = filteredAlunosData.slice(indexOfFirstItem, indexOfLastItem);

  const totalPages = Math.ceil(filteredAlunosData.length / itemsPerPage);

  const handlePageChange = (pageNumber: SetStateAction<number>) => {
    setCurrentPage(pageNumber);
  };

  const generatePDF = () => {
    const input = document.getElementById('pdfContent');
    if (input) {
      html2canvas(input, { scrollY: -window.scrollY }).then(canvas => {
        const totalPages = Math.ceil(filteredAlunosData.length / itemsPerPage);
        const pdf = new jsPDF('p', 'mm', 'a4');
        let y = 20; // Initial position
  
        // Add title
        pdf.setFontSize(18);
        pdf.text("Alunos com Turmas e Aulas", 105, y, { align: 'center' });
        y += 10;
  
        // Add logo or header image if needed
        // const imgPath = `${process.env.PUBLIC_URL}/image/logo.jpg`;
        // pdf.addImage(imgPath, 'JPEG', 15, y, 30, 10);
        // y += 15;
  
        // Prepare data for table
        const tableData: string[][] = [];
        filteredAlunosData.forEach(alunoData => {
          const alunoNome = alunoData.alunos[0]?.nome || '';
          const turmas = alunoData.turmas ? alunoData.turmas.map(turma => turma.nome).join(', ') : '';
          const aula = alunoData.aula ? `${alunoData.aula.licao} (${formatLocalDate(alunoData.aula.dia, 'dd/MM/yyyy')})` : '';
          tableData.push([alunoNome, turmas, aula]);
        });
  
        // Define table columns and format data
        const tableColumns = ["Aluno", "Turmas", "Última Aula"];
  
        // Generate table
        pdf.autoTable({
          startY: y,
          head: [tableColumns],
          body: tableData,
          didDrawCell: (data: { row: { index: number; }; }) => {
            // Example: applying style to every second row
            if (data.row.index % 2 === 0) {
              pdf.setFillColor(240, 240, 240); // Background color for even rows
              pdf.setFontSize(10);
            } else {
              pdf.setFillColor(255, 255, 255); // Background color for odd rows
              pdf.setFontSize(10);
            }
          }
        });
  
        // Add date at the bottom
        const date = new Date();
        const formattedDate = `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
        const xPosition = 10;
        const yPosition = pdf.internal.pageSize.height - 10;
        pdf.setFontSize(8);
        pdf.text(`Data de criação: ${formattedDate}`, xPosition, yPosition);
  
        // Save PDF
        pdf.save('alunos-turmas-aulas.pdf');
      });
    }
  };

 
  return (
    <Container>
      <h2>Alunos com Turmas e Aulas</h2>
      <FormControl
        type="text"
        placeholder="Pesquisar por aluno"
        value={searchTerm}
        onChange={handleSearchChange}
        className="mb-3"
      />
      <div id="pdfContent">
        <Table striped bordered hover variant="dark">
          <thead>
            <tr>
              <th>Aluno(a)</th>
              <th>Turma</th>
              <th>Aulas</th>
            </tr>
          </thead>
          <tbody>
            {currentItems.map((alunoData, index) => (
              <tr key={index}>
                <td>{alunoData.alunos[0]?.nome}</td>
                <td>
                  <ul>
                    {alunoData.turmas && alunoData.turmas.map((turma, turmaIndex) => (
                      <li key={turmaIndex}>{turma.nome}</li>
                    ))}
                  </ul>
                </td>
                <td>
                <ul>
                  {alunoData.aula && (
                    <li>{alunoData.aula.licao}({formatLocalDate(alunoData.aula.dia,'dd/MM/yyyy')})</li>
                  )}
                </ul>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
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
    </Container>
  );
}

export default AlunoTurma;
