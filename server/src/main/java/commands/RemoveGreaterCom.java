package commands;

import data.*;
import collection.CollectionManager;
import messages.AnswerMsg;
import messages.Status;
import messages.User;


/**
 * Remove greatest command
 */
public class RemoveGreaterCom extends AbstractCommand{
    CollectionManager collection;

    public RemoveGreaterCom(CollectionManager col)
    {
        super("remove_greater", " {element} : удалить из коллекции все элементы, превышающие заданный");
        collection = col;
    }

    @Override
    public boolean execute(String argument, Object objArg, AnswerMsg answerMsg, User user) {
        int ans = collection.deleteGreater((Flat) objArg);
        answerMsg.addMsg("Удалено " + ans + " элементов");
        answerMsg.setStatus(Status.OK);
        return true;
    }
}
