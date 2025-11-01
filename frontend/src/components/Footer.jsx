import { FaLinkedin, FaGithub, FaGlobe } from "react-icons/fa";

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-top">
        <div className="footer-project">
          <img src="/gis-de-cera.png" alt="Logo Brincadeiras" className="footer-logo" />
          <span className="project-name">Brincadeiras</span>
        </div>

        <div className="footer-author">
          Criado por <strong>Piter Gomes</strong> — © 2025
        </div>

        <div className="footer-links">
          <a href="https://www.linkedin.com/in/piter-gomes-4a39281a1/" target="_blank" rel="noopener noreferrer" aria-label="LinkedIn">
            <FaLinkedin />
          </a>
          <a href="https://github.com/pitercoding" target="_blank" rel="noopener noreferrer" aria-label="GitHub">
            <FaGithub />
          </a>
          <a href="https://portfolio-pitergomes.vercel.app/" target="_blank" rel="noopener noreferrer" aria-label="Portfolio">
            <FaGlobe />
          </a>
        </div>
      </div>
    </footer>
  );
}

export default Footer;