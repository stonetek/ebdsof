import { Button } from "react-bootstrap";
import DataTableAula from "../../components/tables/tableAula";
import Header from "../../components/header/header";
import Menu from "../../components/menu/Menu";
import Footer from "../../components/footer/footer";

function Aula() {
    return (
        <>
            <div className="min-h-screen flex flex-col bg-slate-100">
                <Header />
                <Menu />

                <div className="flex justify-center mt-4">
                    <h3 className="text-2xl font-bold text-slate-800 text-center">
                        Lista Aulas
                    </h3>
                </div>

                <div className="flex flex-col flex-1 p-4 md:p-6 lg:p-8">
                    <DataTableAula />

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

export default Aula;
