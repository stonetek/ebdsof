import Button from "react-bootstrap/esm/Button";
import DataTableIgreja from "../../components/tables/tableIgreja";
import Header from "../../components/header/header";
import Menu from "../../components/menu/Menu";
import Footer from "../../components/footer/footer";

function Igreja() {
    return (
        <>
            <div className="min-h-screen flex flex-col bg-slate-100">
                <Header />
                <Menu />

                <div className="flex justify-center mt-4">
                    <h3 className="text-2xl font-bold text-slate-800 text-center">
                        Lista Igrejas
                    </h3>
                </div>

                <div className="flex-1 flex flex-col p-4 md:p-6 lg:p-8">
                    <DataTableIgreja />

                    <div className="flex justify-start mt-4">
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

export default Igreja;
