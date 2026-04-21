import { useState, useEffect } from 'react';
import axios from 'axios';

export default function Portfolio() {
  const [data, setData] = useState(null);
  const userId = 1;

  useEffect(() => {
    fetchPortfolio();
  }, []);

  const fetchPortfolio = () => {
    axios.get(`http://localhost:8080/api/portfolio/${userId}`)
      .then(res => setData(res.data))
      .catch(err => console.error(err));
  };

  if (!data) return <div style={{marginTop: '2rem'}}>Loading portfolio...</div>;

  const { user, portfolio } = data;
  const assets = portfolio.assets || [];

  return (
    <div>
      <h1 style={{marginBottom: 8}}>Your Portfolio</h1>
      <p className="text-muted" style={{marginBottom: '2rem'}}>Manage your crypto assets</p>

      <div className="panel" style={{marginBottom: '2rem'}}>
        <h2>Fiat Balance</h2>
        <div className="text-lg font-bold text-success" style={{marginTop: '0.5rem'}}>
          ${Number(user.balance).toLocaleString()}
        </div>
      </div>

      <h2>Crypto Assets</h2>
      <div className="grid-3" style={{marginTop: '1rem'}}>
        {assets.length === 0 ? (
          <p className="text-muted panel">You do not own any crypto currently.</p>
        ) : (
          assets.map(asset => (
            <div key={asset.id} className="panel flex flex-col gap-2">
              <div className="font-bold">{asset.coinId.toUpperCase()}</div>
              <div className="flex justify-between">
                <span className="text-muted">Quantity:</span>
                <span>{asset.quantity}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-muted">Avg Buy Price:</span>
                <span>${Number(asset.averageBuyPrice).toLocaleString()}</span>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
