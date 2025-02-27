import React, { useState, useEffect } from 'react';
import axios from 'axios';

const AppBanner = () => {
  const [config, setConfig] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchConfig = async () => {
      try {
        const response = await axios.get('/api/config/ui');
        setConfig(response.data);
        setLoading(false);
      } catch (err) {
        console.error('Error fetching config:', err);
        setError('Failed to load application configuration');
        setLoading(false);
      }
    };

    fetchConfig();
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return null; // Don't show banner on error
  if (!config['app.banner.enabled']) return null; // Don't show if disabled

  const bannerStyle = {
    backgroundColor: config['app.banner.backgroundColor'] || '#FFCC00',
    color: config['app.banner.textColor'] || '#333333',
    padding: '8px 16px',
    textAlign: 'center',
    fontWeight: 'bold',
    width: '100%',
    position: 'fixed',
    top: 0,
    left: 0,
    zIndex: 1000
  };

  return (
    <div className="app-banner" style={bannerStyle}>
      {config['app.banner.text'] || 'Application Environment'}
      {config['app.environment'] && `: ${config['app.environment']}`}
    </div>
  );
};

export default AppBanner;