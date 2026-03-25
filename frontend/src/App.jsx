import { BrowserRouter, Routes, Route } from "react-router-dom";
import SignIn from "./pages/SignIn"; 
import SignUp from "./pages/SignUp"; 
import Home from "./pages/Home";
import CreatePost from './pages/CreatePost';
import Profile from './pages/Profile';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<SignIn />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/home" element={<Home/>}></Route>
        <Route path="/create-post" element={<CreatePost />} />
        <Route path="/profile" element={<Profile />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;