package com.kartaca.slcm.api.specification;

import com.kartaca.slcm.api.filter.Parameters;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.kartaca.slcm.data.model.postgresql.CustomerOrder;
import com.kartaca.slcm.data.model.postgresql.Product;
import com.kartaca.slcm.data.model.postgresql.SubscriptionModel;
import com.kartaca.slcm.data.model.postgresql.Subscription;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

public class QuerySpecs {
  public static List<String> formatStrings = Arrays.asList("EEE MMM dd HH:mm:ss z yyyy", "yyyy-MM-dd hh:mm", "yyyy-MM-dd hh:mm:ss");

    private static final Logger logger = getLogger(QuerySpecs.class);

  
  public static Date parseDate(String dateString) throws ParseException{
    for (String formatString : formatStrings)
    {
        try
        {
            return new SimpleDateFormat(formatString).parse(dateString);
        }
        catch (ParseException e) {}
    }
    
    return null;
}
  public static List<Predicate> compare(Root root,Map<String,Join> rootmap, Map <String,Class> objmap ,CriteriaBuilder builder,Map<String,Parameters> objList){
        List<Predicate> predicates = new ArrayList<>();
        String column_name;   
        Parameters parameters;
        Class mainroottype = root.getModel().getBindableJavaType();
        Iterator iterator = objmap.keySet().iterator();
        String roottype="root";
        while( iterator.hasNext()){
            try{
                
            column_name=(String) iterator.next();
            Class classtype= objmap.get(column_name);
            if (classtype.toString().equals(mainroottype.toString())){
                roottype="root";
            }
            else{
                roottype="join";
            }
            parameters=(Parameters) objList.get(column_name);

            String op=(String) parameters.getOperator();
            boolean isEnum=false;
            Class cls;
            if (roottype.equals("root")){
                cls=root.get(column_name).getJavaType();
            }
            else{
                cls=rootmap.get(column_name).get(column_name).getJavaType();
            }
            Object value=parameters.getValue();
           
            
            
            if (Number.class.isAssignableFrom(cls)){
                Double doubleValue = Double.valueOf(String.valueOf(value));
                if (roottype.equals("root")){
                    if (op.equals("=")){
                            predicates.add(builder.equal(root.get(column_name), doubleValue));
                    }
                    if (op.equals("<=")){
                            predicates.add(builder.lessThan(root.get(column_name), doubleValue));
                    }
                    if (op.equals(">=")){
                            predicates.add(builder.greaterThan(root.get(column_name), doubleValue));
                    }
                    if (op.equals("between")){
                            Object value2=parameters.getValue2();
                            Double doubleValue2 = Double.valueOf(String.valueOf(value2));
                            predicates.add(builder.between(root.get(column_name), doubleValue,doubleValue2));
                    }
                }
                else{
                    if (op.equals("=")){
                        predicates.add(builder.equal(rootmap.get(column_name).get(column_name), doubleValue));
                    }
                    if (op.equals("<=")){
                        predicates.add(builder.lessThan(rootmap.get(column_name).get(column_name), doubleValue));
                    }
                    if (op.equals(">=")){
                        predicates.add(builder.greaterThan(rootmap.get(column_name).get(column_name), doubleValue));
                    }
                    if (op.equals("between")){
                        Object value2=parameters.getValue2();
                        Double doubleValue2 = Double.valueOf(String.valueOf(value2));
                        predicates.add(builder.between(rootmap.get(column_name).get(column_name), doubleValue,doubleValue2));
                    }
                }
            }
            if (cls.equals(Date.class)){
                Date dateValue = parseDate(String.valueOf(value));
                if (roottype.equals("root")){
                    if (op.equals("=")){
                            predicates.add(builder.equal(root.get(column_name), dateValue));
                    }
                    if (op.equals("<=")){
                            predicates.add(builder.lessThan(root.get(column_name), dateValue));
                    }
                    if (op.equals(">=")){
                            predicates.add(builder.greaterThan(root.get(column_name), dateValue));
                    }
                    if (op.equals("between")){
                            Date dateValue2=parseDate (String.valueOf(parameters.getValue2()));
                            predicates.add(builder.between(root.get(column_name), dateValue,dateValue2));
                    }
                }
                else{
                    if (op.equals("=")){
                        predicates.add(builder.equal(rootmap.get(column_name).get(column_name), dateValue));
                    }
                    if (op.equals("<=")){
                        predicates.add(builder.lessThan(rootmap.get(column_name).get(column_name), dateValue));
                    }
                    if (op.equals(">=")){
                        predicates.add(builder.greaterThan(rootmap.get(column_name).get(column_name), dateValue));
                    }
                    if (op.equals("between")){
                        Date dateValue2=parseDate (String.valueOf(parameters.getValue2()));
                        predicates.add(builder.between(rootmap.get(column_name).get(column_name), dateValue,dateValue2));
                    }
                }
            }
            else{
                if (roottype.equals("root")){
                    if (op.equals("=") || op.equals("like")){
                            predicates.add(builder.equal(root.get(column_name).as(String.class), String.valueOf(value)));
                    }
                }
                else{
                    if (op.equals("=") || op.equals("like")){
                        predicates.add(builder.equal(rootmap.get(column_name).get(column_name).as(String.class), String.valueOf(value)));
                    }
                }
            }
        }
        catch(NullPointerException e){
        }catch(Exception e){
              logger.error("error occured at query specifications: "+e.getMessage());
        }
        }
        return predicates;
  }
  public static Specification<Subscription> customSubQuery (Map <String,Class>objmap,Map<String,Parameters> objList){
      return new Specification<Subscription>() {
        public Predicate toPredicate(Root<Subscription> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Join<Subscription,Product> productjoint = root.join("product");
            Map rootmap = new HashMap();
            Iterator iterator = objmap.keySet().iterator();
            while (iterator.hasNext()){
                String column_name=(String) iterator.next();
                
                if (objmap.get(column_name).toString().equals(Product.class.toString())){
                    rootmap.put(column_name,productjoint);
                }
            }
            
            
            List<Predicate> predicates=compare(root,rootmap,objmap, cb, objList);
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        }
    };
  }
  public static Specification<CustomerOrder> customOrderQuery (Map <String,Class>objmap,Map<String,Parameters> objList){
      return new Specification<CustomerOrder>() {
          
        public Predicate toPredicate(Root<CustomerOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Join<CustomerOrder,Subscription> subscriptionjoint = root.join("subscription");
            Join<Subscription,SubscriptionModel> modeljoint = subscriptionjoint.join("subscriptionModel");
            Map rootmap = new HashMap();
            Iterator iterator = objmap.keySet().iterator();
            while (iterator.hasNext()){
                String column_name=(String) iterator.next();
                if (objmap.get(column_name).toString().equals(Subscription.class.toString())){
                    rootmap.put(column_name,subscriptionjoint);
                }
                if (objmap.get(column_name).toString().equals(SubscriptionModel.class.toString())){
                    rootmap.put(column_name,modeljoint);
                }
                
            }
            List<Predicate> predicates=compare(root,rootmap,objmap, cb, objList);
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        }
    };
      
  }
  public static Specification<SubscriptionModel> customModelQuery(Map <String,Class>objmap,Map<String,Parameters> objList) {
       return (root, query, builder) -> {
            Map rootmap = new HashMap();
            List<Predicate> predicates=compare(root,rootmap,objmap, builder, objList);
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
      }; 
    }
}