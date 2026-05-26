import { Button } from "react-bootstrap";
import DataTableTurma from "../../components/tables/tableTurma";
import Header from "../../components/header/header";
import Footer from "../../components/footer/footer";
//import Menu from "../../components/menu/Menu";


function Turma(){

   return (
    <>
        <div className="min-h-screen flex flex-col bg-slate-100">

            <Header />

            {/* Título */}
            <div className="flex justify-center mt-4">
                <h3 className="text-2xl font-bold text-slate-800 text-center">
                    Lista Classes
                </h3>
            </div>

            {/* Conteúdo */}
            <div className="flex flex-col flex-1 p-4 md:p-6 lg:p-8">

                <DataTableTurma />

                {/* Botão voltar */}
                <div className="flex w-full mt-4">

                    <Button
                        variant="primary"
                        className="btn-primary"
                        as="a"
                        href="/home"
                    >
                        VOLTAR
                    </Button>

                </div>

            </div>

            <Footer />

        </div>
    </>
)
}

export default Turma;
