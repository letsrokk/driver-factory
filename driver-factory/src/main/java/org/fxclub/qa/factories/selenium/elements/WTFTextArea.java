package org.fxclub.qa.factories.selenium.elements;

import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.TextInput;

import java.util.Optional;

/**
 * Created by majer-dy on 12/05/2017.
 */
public class WTFTextArea extends TextInput {

    public WTFTextArea(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public String getText() {
        return Optional.of(this.getWrappedElement().getAttribute("value")).orElse(this.getWrappedElement().getText().trim());
    }
}
