/* eslint-disable @typescript-eslint/no-unused-vars */
import { useState } from "react";
import Aniversários from "../../components/tables/tableAniversarios";
import ProfessorTurma from "../../components/tables/tableProfessorTurma";
import { animated, useSpring } from "react-spring";
import Header from "../../components/header/header";
import AlunoTurma from "../../components/tables/tableAlunoTurma";
import Footer from "../../components/footer/footer";
import { Button } from "react-bootstrap";

function Vinculos () {

    const [selectedVinculo, setSelectedVinculo] = useState('empresaNatureza');

    const [clickedVinculo, setClickedVinculo] = useState<string | null>(null);

    const renderVinculoTable = () => {
        switch(selectedVinculo) {
            case 'aniversarios':
                return <Aniversários />;
            case 'professorTurma':
                return <ProfessorTurma />;
            case 'professorAula':
                return <AlunoTurma />;
            default:
                return null;
        }
    }

    const [springProps] = useSpring(() => ({
        transform: 'scale(1)',
    }));

    const handleClick = (vinculo: string) => {
        setSelectedVinculo(vinculo);
        setClickedVinculo(vinculo);
        //setTimeout(() => setClickedVinculo(null), 200);
    };


    return (
  <>
    <Header/>
    <div className="flex flex-col md:flex-row w-full min-h-screen">
      
      {/* Menu lateral */}
      <div className="bg-gray-300 w-full md:w-1/6 p-4">
        <h1 className="text-2xl md:text-4xl mb-5">Escolha um Relatório</h1>
        <ul>
          <animated.li 
            style={springProps}
            className="hover:border hover:border-gray-500 hover:text-red-700 rounded mb-2 cursor-pointer"
            onClick={() => handleClick('aniversarios')}
          >
            Aniversários
          </animated.li>
          <animated.li 
            style={springProps}
            className="hover:border hover:border-gray-500 hover:text-red-700 rounded mb-2 cursor-pointer"
            onClick={() => handleClick('professorTurma')}
          >
            Professor e Turma
          </animated.li>
          <animated.li 
            style={springProps}
            className="hover:border hover:border-gray-500 hover:text-red-700 rounded cursor-pointer"
            onClick={() => handleClick('professorAula')}
          >
            Aluno e Turma
          </animated.li>
        </ul>

        {clickedVinculo === null && (
          <div className="mt-4">
            <Button variant="primary" as="a" href="/home">VOLTAR</Button>
          </div>
        )}
      </div>

      {/* Conteúdo principal */}
      <div className="flex-1 p-4 md:p-8">
        <h1 className="mt-6 text-xl md:text-2xl text-center">RELATÓRIOS</h1>
        <div className="mx-auto mt-6 text-center">
          <div className="inline-block w-full overflow-x-auto">
            {renderVinculoTable()}
          </div>
        </div>
      </div>
    </div>    
    <Footer />
  </>
);

}

export default Vinculos;