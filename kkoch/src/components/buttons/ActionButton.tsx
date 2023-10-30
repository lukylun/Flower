import { useNavigate } from "react-router";
import "./ActionButton.css"

type Props = {
	link: string;
	msg: string;
}

const ActionButton = ({link, msg } : Props) => {
  const navigate = useNavigate();

  const handleLink = () => {
    navigate(link)
  }

  return (
    <button className="cssbuttons-io-button" onClick={handleLink}> {msg}
      <div className="icon">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24"><path fill="none" d="M0 0h24v24H0z"></path><path fill="currentColor" d="M16.172 11l-5.364-5.364 1.414-1.414L20 12l-7.778 7.778-1.414-1.414L16.172 13H4v-2z"></path></svg>
      </div>
    </button>
  )
}

export default ActionButton