/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/no-explicit-any */
import jsPDF from 'jspdf';
import 'jspdf-autotable';
import { Relatorio } from '../../types/relatorio';

const generatePDF = (relatorio: Relatorio | null, ebdNome: string, dataInput: string) => {
    const doc = new jsPDF();

    const formatDate = (input: string): string => {
        const today = new Date();
        let year = today.getFullYear();
        let month = today.getMonth() + 1;
        const day = today.getDate();
        const fullDateRegex = /^\d{1,2}\/\d{1,2}\/\d{4}$/;
        if (fullDateRegex.test(input)) {
            const [d, m, y] = input.split('/').map(Number);
            return new Date(y, m - 1, d).toLocaleDateString();
        }
        if (input.length === 4 && !isNaN(Number(input))) {
            year = Number(input);
            return `${day}/${month}/${year}`;
        }
        
        const quarterRegex = /^(1|2|3|4)\/\d{4}$/;
        if (quarterRegex.test(input)) {
            const [quarter, y] = input.split('/').map(Number);
            year = y;
            month = (quarter - 1) * 3 + 1;
            return `${day}/${month}/${year}`;
        }
        return `${day}/${month}/${year}`; 
    };
   
    const formattedDate = formatDate(dataInput);

    doc.setFont('Helvetica', 'normal');
    doc.setFontSize(20);
    doc.text('Relatório de Secretaria', 14, 20);
    
    doc.setFontSize(12);
    doc.text(`EBD: ${ebdNome}`, 14, 30);
    doc.text(`Data: ${formattedDate}`, 14, 40);

    if (relatorio) {
        const tableData = [
            [
                'Total Alunos Matriculados',
                'Total Alunos Presentes',
                'Total Alunos Ausentes',
                'Total Visitantes',
                'Total Assistência',
                'Total Bíblias',
                'Total Revistas',
                'Total Oferta'
            ],
            [
                relatorio.totalMatriculados.toString(),
                relatorio.totalPresentes.toString(),
                relatorio.totalAusentes.toString(),
                relatorio.totalVisitantes.toString(),
                relatorio.totalAssistencia.toString(),
                relatorio.totalBiblias.toString(),
                relatorio.totalRevistas.toString(),
                relatorio.totalOferta.toFixed(2)
            ]
        ];

        (doc as any).autoTable({
            head: tableData.slice(0, 1),
            body: tableData.slice(1),
            startY: 50,
            margin: { horizontal: 14 },
            styles: {
                cellPadding: 3,
                fontSize: 8,
                halign: 'center',
            },
            columnStyles: {
                0: { cellWidth: 'auto' },
                1: { cellWidth: 'auto' },
                2: { cellWidth: 'auto' },
                3: { cellWidth: 'auto' },
                4: { cellWidth: 'auto' },
                5: { cellWidth: 'auto' },
                6: { cellWidth: 'auto' },
                7: { cellWidth: 'auto' },
            },
        });

        if (relatorio.turmas && relatorio.turmas.length > 0) {
            doc.addPage();

            doc.setFontSize(16);
            doc.text('Relatório por Turma', 14, 20);

            const turmaTableHeader = [
                'Nome da Turma',
                'Total Matriculados',
                'Total Presentes',
                'Total Ausentes',
                'Total Visitantes',
                'Total Assistência',
                'Total Bíblias',
                'Total Revistas',
                'Total Oferta'
            ];

            const turmaTableData = relatorio.turmas.map(turma => [
                turma.nomeTurma,
                turma.totalMatriculados.toString(),
                turma.totalPresentes.toString(),
                turma.totalAusentes.toString(),
                turma.totalVisitantes.toString(),
                turma.totalAssistencia.toString(),
                turma.totalBiblias.toString(),
                turma.totalRevistas.toString(),
                turma.totalOferta.toFixed(2)
            ]);

            (doc as any).autoTable({
                head: [turmaTableHeader],
                body: turmaTableData,
                startY: 30,
                margin: { horizontal: 14 },
                styles: {
                    cellPadding: 3,
                    fontSize: 7,
                    halign: 'center',
                },
                columnStyles: {
                    0: { cellWidth: 'auto' },
                    1: { cellWidth: 'auto' },
                    2: { cellWidth: 'auto' },
                    3: { cellWidth: 'auto' },
                    4: { cellWidth: 'auto' },
                    5: { cellWidth: 'auto' },
                    6: { cellWidth: 'auto' },
                    7: { cellWidth: 'auto' },
                    8: { cellWidth: 'auto' },
                },
            });
        }
    }

    doc.save('relatorio_secretaria.pdf');
};

export default generatePDF;
