import { useState } from 'react'; // Eksikti, eklendi
import { useNavigate } from 'react-router-dom'; // Eksikti, eklendi
import { authAPI } from '../services/UserService';
import bg from "../assets/bg.webp";
import Input from "../components/Input";
import Button from '../components/Button';

export default function SignIn() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const response = await authAPI.login({
        email,
        password
      });

      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data));

      console.log("Giriş verisi:", response.data);
      alert("Login succesful!");
      navigate("/home");
      
    } catch (err) {
      console.error(err);
      alert("Login is failed: " + (err.response?.data?.message || "Bilgilerinizi kontrol edin"));
    }
  };

  return (
    <div className="min-h-screen p-15 flex flex-col items-center justify-center bg-cover bg-center bg-no-repeat" style={{ backgroundImage: `url(${bg})` }}
>
      <h1 className="text-4xl font-extrabold text-gray-900 mb-4">UniEvent</h1>

      <div className="flex flex-col items-center justify-center my-5 w-full"> 
        <div className="flex flex-col gap-5 bg-gray-800/30 p-12 rounded-xl shadow-2xl max-w-md w-full backdrop-blur-sm">
          <h1 className="text-4xl font-extrabold text-gray-300">Welcome Back</h1>
          
          <p className="text-lg font-semibold text-gray-300">
            Don't have an account? 
            <span 
              onClick={() => navigate('/signup')} 
              className="ml-2 cursor-pointer text-gray-900 hover:underline font-bold"
            >
              Sign Up
            </span>
          </p>
          <Input 
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            type="email"
          />
          <Input 
            placeholder="Password" 
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            type="password"
          />
          <Button text="Login" onClick={handleLogin}/>
        </div>
      </div>
    </div>
  );
}