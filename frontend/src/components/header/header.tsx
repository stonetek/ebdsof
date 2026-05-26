import { Link, useLocation } from 'react-router-dom'
import LOGO from '../../../public/image/biblia.jpg'
import avatarImage from '../../../public/image/avatarImage.jpg'

type HeaderProps = {
  hideWelcome?: boolean;
  showOnlyWelcome?: boolean;
}

function HomeIcon({ className = 'h-5 w-5' }: { className?: string }) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 24 24"
      fill="currentColor"
      className={className}
    >
      <path d="M3 10.5L12 3l9 7.5V20a1 1 0 0 1-1 1h-5v-6H9v6H4a1 1 0 0 1-1-1V10.5z" />
    </svg>
  )
}

function Header({ hideWelcome = false, showOnlyWelcome = false }: HeaderProps) {
  const storedNomeUsuario = sessionStorage.getItem('nomeUsuario') || '';
  const location = useLocation();
  const showHomeButton = location.pathname !== '/home' && location.pathname !== '/';
  const welcomeText = showOnlyWelcome
    ? 'Bem-vindo(a)'
    : `Bem-vindo(a), ${storedNomeUsuario || 'Usuário'}`;

  return (
    <div className="w-full bg-gradient-to-r from-slate-700 px-3 py-3 sm:px-4">
      {/* MOBILE */}
      <div className="flex items-center justify-between md:hidden">

        {/* LOGO + TEXTO */}
        <div className="flex items-center gap-3 min-w-0">
          <img
            src={LOGO}
            alt="logo"
            className="h-14 w-14 rounded-xl object-cover shadow-md"
          />

          <div className="min-w-0">
            <h1 className="text-2xl font-semibold text-black leading-tight">
              EBDSoft
            </h1>

            <p className="text-xs text-cyan-400 leading-tight">
              Developer{' '}
              <a
                href="https://www.instagram.com/pedrors99999/"
                target="_blank"
                rel="noopener noreferrer"
                className="underline transition hover:text-cyan-200"
              >
                Pedro Paulo
              </a>
            </p>
          </div>
        </div>

          {/* BOTÕES + USUÁRIO */}
          <div className="flex flex-col items-center gap-2 shrink-0">

            {showHomeButton && (
              <Link
                to="/home"
                title="Home"
                className="flex h-9 w-9 items-center justify-center rounded-lg bg-blue-500 text-white shadow-md transition hover:bg-blue-600"
              >
                <HomeIcon className="h-5 w-5" />
              </Link>
            )}

            {!hideWelcome && (
              <div className="flex flex-col items-center">
                <img
                  src={avatarImage}
                  alt="Avatar"
                  className="h-9 w-9 rounded-full border-2 border-white shadow-md"
                />

                <div className='-mt-10'>

                  <span className="max-w-[90px] text-center text-[10px] font-medium leading-tight text-sky-300 break-words">
                    {welcomeText}
                  </span>
                </div>

              </div>
            )}
          </div>
      </div>

      {/* DESKTOP */}
      <div className="hidden place-items-start justify-between md:flex">
        <div className="flex min-w-0 items-center gap-4">
          <img
            src={LOGO}
            alt="logo"
            className="h-20 w-20 rounded-2xl object-cover"
          />

          <div className="min-w-0">
            <h1 className="text-4xl font-semibold text-black">
              EBDSoft
            </h1>

            <p className="text-base text-cyan-400">
              Developer por{' '}
              <a
                href="https://www.instagram.com/pedrors99999/"
                target="_blank"
                rel="noopener noreferrer"
                className="underline transition hover:text-cyan-200"
              >
                Pedro Paulo
              </a>
            </p>
          </div>
        </div>

        <div className="flex shrink-0 flex-col items-center gap-4">
          {showHomeButton && (
            <Link
              to="/home"
              title="Home"
              className="inline-flex h-10 w-10 items-center justify-center rounded-xl border border-slate-500 bg-blue-500 text-slate-100 shadow-md transition hover:bg-slate-700 lg:h-11 lg:w-11"
            >
              <HomeIcon />
            </Link>
          )}

          {!hideWelcome && (
            <div className="flex flex-col items-center gap-2">
              <img
                src={avatarImage}
                alt="Avatar"
                className="h-11 w-11 rounded-full border border-white lg:h-12 lg:w-12"
              />

              <span className="text-sm font-medium leading-tight text-sky-300 whitespace-nowrap lg:text-base">
                {welcomeText}
              </span>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default Header;
