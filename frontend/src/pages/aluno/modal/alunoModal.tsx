import React from 'react';
import { Modal, Button } from 'react-bootstrap';

interface Aluno {
  id: number;
  nome: string;
  presente?: boolean | null;
}

interface AlunoAulas {
   id: number;
  idAluno: number;
  nome: string;
  presente: boolean;
}

interface AlunosModalProps {
  show: boolean;
  handleClose: () => void;
  alunosPorTurma: Aluno[];
  alunoAulas: AlunoAulas[];
  turma: string;
}

const AlunosModal: React.FC<AlunosModalProps> = ({ show, handleClose, alunosPorTurma, alunoAulas, turma }) => {
  const alunoPresencaMap = new Map<number, boolean>();
  alunoAulas.forEach((aluno) => {
    alunoPresencaMap.set(aluno.idAluno, aluno.presente);
  });

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Alunos da Turma: {turma}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <ul>
          {alunosPorTurma.map((aluno, index) => {
            const presente = alunoPresencaMap.get(aluno.id) ?? false;
            return (
              <li key={`${aluno.id}-${index}`} style={{ color: presente ? 'green' : 'red' }}>
                {aluno.nome}
              </li>
            );
          })}
        </ul>
        <div className="mt-3">
          <p><span style={{ color: 'green' }}>●</span> Presente</p>
          <p><span style={{ color: 'red' }}>●</span> Ausente</p>
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Fechar
        </Button>
      </Modal.Footer>
    </Modal>
  );
};



export default AlunosModal;
