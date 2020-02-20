package com.tabjy.snippets.jfr;

import jdk.jfr.Configuration;
import jdk.jfr.FlightRecorder;
import jdk.jfr.Recording;
import org.openjdk.jmc.common.item.*;
import org.openjdk.jmc.common.unit.IQuantity;
import org.openjdk.jmc.flightrecorder.CouldNotLoadRecordingException;
import org.openjdk.jmc.flightrecorder.JfrLoaderToolkit;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class RuntimeEvents {
    public static void main(String[] args)
            throws IOException, ParseException, InterruptedException, CouldNotLoadRecordingException {
        try {
            Instant then = Instant.now();
            Recording r = new Recording(Configuration.getConfiguration("default"));
            r.start();

            Thread.sleep(5000);
            System.out.println(Duration.between(then, Instant.now())); //

            Recording r2 = FlightRecorder.getFlightRecorder().takeSnapshot();
            System.out.println(Duration.between(then, Instant.now())); //
            InputStream is = r2.getStream(then, Instant.now()); // FIXME: slow

            if (r2.getSize() == 0) {
                System.out.println("empty recording");
                return;
            }

            IItemCollection items = JfrLoaderToolkit.loadEvents(is); // FIXME: slow
            System.out.println(Duration.between(then, Instant.now())); //

            if (!items.hasItems()) {
                System.out.println("empty recording");
                return;
            }

            Map<String, List<Double>> result = new HashMap<>();

            for (IItemIterable itemIterable : items) {
                // each itemIterable is an array of events sharing a single type.
                IType<IItem> type = itemIterable.getType();
                List<IAttribute<?>> attributes = type.getAttributes();
                for (IAttribute<?> attribute : attributes) {
                    IMemberAccessor<?, IItem> accessor = ItemToolkit.accessor(attribute);

                    String query =
                            type.getIdentifier().replaceAll("\\.", "_") + "{attribute=\"" + attribute.getIdentifier()
                                    + "\"}";
                    for (IItem item : itemIterable) {
                        if (!(accessor.getMember(item) instanceof IQuantity)) {
                            break;
                        }

                        Double data = ((IQuantity) accessor.getMember(item)).doubleValue();
                        if (data.isNaN()) {
                            continue;
                        }

                        result.compute(query, (k, v) -> {
                            if (v == null) {
                                v = new LinkedList<>();
                            }
                            v.add(data);
                            return v;
                        });
                    }
                }
            }

            List<Map.Entry<String, List<Double>>> entries = new ArrayList(result.entrySet());
            entries.sort(Comparator.comparing(Map.Entry::getKey));
            for (Map.Entry<String, List<Double>> entry : entries) {
                String query = entry.getKey();
                double sum = 0;
                for (Double item : entry.getValue()) {
                    sum += item;
                }

                double avg = sum / entry.getValue().size();
                if (entry.getValue().size() == 0) {
                    avg = 0;
                }

                System.out.println(query + " " + avg);
            }
            System.out.println(Duration.between(then, Instant.now()));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }
}
