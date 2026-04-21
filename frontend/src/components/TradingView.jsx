import { useState, useEffect } from 'react';
import axios from 'axios';

export default function TradingView() {
  const [coins, setCoins] = useState([]);
  const [selectedCoin, setSelectedCoin] = useState('');
  const [quantity, setQuantity] = useState('');
  const [type, setType] = useState('BUY');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  const userId = 1;

  useEffect(() => {
    axios.get('http://localhost:8080/api/market')
      .then(res => {
        setCoins(res.data);
        if (res.data.length > 0) setSelectedCoin(res.data[0].id);
      })
      .catch(console.error);
  }, []);

  const handleTrade = (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');
    setError('');

    axios.post('http://localhost:8080/api/trade/execute', {
      userId,
      coinId: selectedCoin,
      quantity: quantity,
      type
    })
      .then(res => {
        setMessage(res.data.message);
        setQuantity('');
      })
      .catch(err => {
        setError(err.response?.data?.error || 'Trade failed');
      })
      .finally(() => setLoading(false));
  };

  return (
    <div style={{maxWidth: 600, margin: '0 auto'}}>
      <h1 style={{marginBottom: 8}}>Execute Trade</h1>
      <p className="text-muted" style={{marginBottom: '2rem'}}>Buy or sell crypto instantly</p>

      <form onSubmit={handleTrade} className="panel flex flex-col gap-4">
        {message && <div style={{padding: '1rem', background: 'var(--success-bg)', color: 'var(--success)', borderRadius: 8}}>{message}</div>}
        {error && <div style={{padding: '1rem', background: 'var(--danger-bg)', color: 'var(--danger)', borderRadius: 8}}>{error}</div>}

        <div className="input-group">
          <label>Action</label>
          <select className="input" value={type} onChange={e => setType(e.target.value)} style={{color: "white"}}>
            <option value="BUY">Buy</option>
            <option value="SELL">Sell</option>
          </select>
        </div>

        <div className="input-group">
          <label>Asset</label>
          <select className="input" value={selectedCoin} onChange={e => setSelectedCoin(e.target.value)} style={{color: "white"}}>
            {coins.map(c => <option key={c.id} value={c.id} style={{color: "black"}}>{c.name} ({c.symbol.toUpperCase()}) - ${c.current_price}</option>)}
          </select>
        </div>

        <div className="input-group">
          <label>Quantity</label>
          <input 
            type="number" 
            step="0.00000001"
            required
            className="input" 
            value={quantity} 
            onChange={e => setQuantity(e.target.value)}
            placeholder="0.00"
          />
        </div>

        <button type="submit" className={`btn btn-${type === 'BUY' ? 'success' : 'danger'}`} disabled={loading} style={{marginTop: '1rem', padding: '1rem', fontSize: '1.1rem'}}>
          {loading ? 'Executing...' : `Execute ${type}`}
        </button>
      </form>
    </div>
  );
}
