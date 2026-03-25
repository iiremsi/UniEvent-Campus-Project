import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authAPI } from '../services/UserService';
import bg from "../assets/bg.webp";
import Input from "../components/Input";
import Button from '../components/Button';
import { AiOutlineArrowLeft } from "react-icons/ai";

export default function SignUp() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: ''
  });
  const [error, setError] = useState('');

  const handleSubmit = async () => {
    try {
      await authAPI.register({
        username: formData.username,
        email: formData.email,
        password: formData.password,
        accountType: 'STUDENT',
        displayName: formData.username
      });
      alert('Succesfully registered!');
      navigate('/'); 
    } catch (err) {
      setError('Hata: ' + (err.response?.data || 'error'));
    }
  };

  return (
    <div className="min-h-screen p-15 flex flex-col items-center justify-center bg-cover bg-center bg-no-repeat relative" style={{ backgroundImage: `url(${bg})` }}>
      
      {/* GERİ TUŞU */}
      <button 
        onClick={() => navigate('/')} 
        className="absolute top-8 left-8 p-3 bg-gray-800/30 hover:bg-gray-800 text-white rounded-xl backdrop-blur-sm border border-gray-700 shadow-xl flex items-center justify-center"
      >
        <AiOutlineArrowLeft size={24} />
      </button>

      <h1 className="text-4xl font-extrabold text-gray-800">UniEvent</h1>

      <div className="flex flex-col items-center justify-center my-5"> 
        <div className="flex flex-col gap-5 bg-gray-800/30 p-12 rounded-xl shadow-2xl max-w-4xl w-full backdrop-blur-sm">
          <h1 className="text-4xl font-extrabold text-gray-300">Hello</h1>
          <p className="text-xl font-bold text-gray-300 gap-3">Enter your informations</p>
          
          <Input 
            placeholder="Username"
            value={formData.username}
            onChange={(e) => setFormData({...formData, username: e.target.value})}
          />
          <Input 
            placeholder="Email"
            value={formData.email}
            onChange={(e) => setFormData({...formData, email: e.target.value})}
          />
          <Input 
            placeholder="Password"
            value={formData.password}
            onChange={(e) => setFormData({...formData, password: e.target.value})}
            type="password"
          />
          
          {error && <p className="text-red-400 text-sm font-bold">{error}</p>}
          
          <Button text="Register" onClick={handleSubmit}/>
        </div>
      </div>
    </div>
  );
}