package geodes.sms.gnmf.generator.adapters;

import geodes.sms.neo4j.io.entity.INodeEntity;
import org.eclipse.emf.ecore.EPackage;

public interface IAdapterFactory
{
    IModelAdapter connect(String uri, String user, String password);
    INodeAdapter createAdapter(INodeEntity node) throws AdapterException;

    EPackage getEPackage();
}
