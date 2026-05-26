import { Button } from "react-bootstrap";
import DataTableProfessor from "../../components/tables/tableProfessor";
import Menu from "../../components/menu/Menu";
import Header from "../../components/header/header";
import Footer from "../../components/footer/footer";


function Professor(){

   return (
    <>
        <div className="min-h-screen flex flex-col bg-slate-100">

            <Header />
            <Menu />

            {/* Título */}
            <div className="flex justify-center mt-4">
                <h3 className="text-2xl font-bold text-slate-800 text-center">
                    Lista Professores
                </h3>
            </div>

            {/* Conteúdo */}
            <div className="flex-1 flex flex-col p-4 md:p-6 lg:p-8">

                <DataTableProfessor />

                {/* Botão voltar */}
                <div className="mt-4">
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

export default Professor;