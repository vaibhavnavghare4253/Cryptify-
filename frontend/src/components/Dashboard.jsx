import { useState, useEffect } from 'react';
import axios from 'axios';
import { TrendingUp, TrendingDown } from 'lucide-react';

export default function Dashboard() {
  const [coins, setCoins] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios.get('http://localhost:8080/api/market')
      .then(res => {
        setCoins(res.data);
        setLoading(false);
      })
      .catch(err => console.error(err));
  }, []);

  if (loading) return <div style={{marginTop: '2rem'}}>Loading real-time prices...</div>;

  return (
    <div>
      <h1 style={{marginBottom: 8}}>Market Overview</h1>
      <p className="text-muted" style={{marginBottom: '2rem'}}>Live rates from CoinGecko</p>
      
      <div className="grid-3">
        {coins.map(coin => (
          <div key={coin.id} className="panel flex flex-col gap-3">
            <div className="flex justify-between align-center">
              <div className="flex align-center gap-3">
                <img src={coin.image} alt={coin.name} className="coin-icon" />
                <div>
                  <div className="font-bold">{coin.name}</div>
                  <div className="text-muted">{coin.symbol.toUpperCase()}</div>
                </div>
              </div>
              <div className="text-lg font-bold">${coin.current_price.toLocaleString()}</div>
            </div>
            
            <div className={`flex align-center gap-2 ${coin.price_change_percentage_24h >= 0 ? 'text-success' : 'text-danger'}`}>
              {coin.price_change_percentage_24h >= 0 ? <TrendingUp size={16} /> : <TrendingDown size={16} />}
              <span>{Math.max(0, coin.price_change_percentage_24h || 0).toFixed(2)}% (24h)</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
