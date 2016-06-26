package Consumer;

import java.io.IOException;

public interface ConsumerIF {
	/**
	 * Register the Consumer on the Server
	 * 
	 * @return if the Operation has been succesful
	 * @throws IOException
	 */
	public boolean registerOnServer() throws IOException;

}
