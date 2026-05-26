import { Button } from "react-bootstrap";
import DataTableEbd from "../../components/tables/tableEbd";
import Menu from "../../components/menu/Menu";
import Header from "../../components/header/header";
import Footer from "../../components/footer/footer";


function Ebd(){

    return (
    <>
        <div className="min-h-screen flex flex-col bg-slate-100">

            <Header />
            <Menu />

            {/* Título */}
            <div className="flex justify-center mt-4">
                <h3 className="text-2xl font-bold text-slate-800 text-center">
                    Lista Escola Bíblica
                </h3>
            </div>

            {/* Conteúdo */}
            <div className="flex-1 flex flex-col p-4 md:p-6 lg:p-8">

                <DataTableEbd />

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

export default Ebd;