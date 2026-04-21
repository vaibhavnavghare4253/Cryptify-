import { BrowserRouter as Router, Routes, Route, Link, useLocation } from 'react-router-dom';
import Dashboard from './components/Dashboard';
import Portfolio from './components/Portfolio';
import TradingView from './components/TradingView';
import { LineChart, Briefcase, Activity } from 'lucide-react';

function Navbar() {
  const location = useLocation();
  return (
    <nav className="navbar">
      <div className="nav-brand">Cryptify</div>
      <div className="nav-links">
        <Link to="/" className={location.pathname === '/' ? 'active' : ''}>
          <div className="flex gap-2 align-center"><LineChart size={18} /> Market</div>
        </Link>
        <Link to="/trade" className={location.pathname === '/trade' ? 'active' : ''}>
          <div className="flex gap-2 align-center"><Activity size={18} /> Trade</div>
        </Link>
        <Link to="/portfolio" className={location.pathname === '/portfolio' ? 'active' : ''}>
          <div className="flex gap-2 align-center"><Briefcase size={18} /> Portfolio</div>
        </Link>
      </div>
    </nav>
  );
}

function App() {
  return (
    <Router>
      <Navbar />
      <main className="main-content container">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/trade" element={<TradingView />} />
          <Route path="/portfolio" element={<Portfolio />} />
        </Routes>
      </main>
    </Router>
  );
}

export default App;
